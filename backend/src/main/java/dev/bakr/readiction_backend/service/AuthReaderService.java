package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.exceptions.ExistsException;
import dev.bakr.readiction_backend.exceptions.InvalidInputsException;
import dev.bakr.readiction_backend.exceptions.NotFoundException;
import dev.bakr.readiction_backend.mappers.ReaderMapper;
import dev.bakr.readiction_backend.model.Reader;
import dev.bakr.readiction_backend.repository.ReaderRepository;
import dev.bakr.readiction_backend.requests.LoginReaderDtoRequest;
import dev.bakr.readiction_backend.requests.RegisterReaderDtoRequest;
import dev.bakr.readiction_backend.requests.VerifyReaderDtoRequest;
import dev.bakr.readiction_backend.responses.LoginReaderDtoResponse;
import dev.bakr.readiction_backend.responses.RegisterReaderDtoResponse;
import dev.bakr.readiction_backend.utils.GenerateVerificationCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthReaderService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
    private static final Logger logger = LoggerFactory.getLogger(AuthReaderService.class);
    private final AuthenticationManager authenticationManager;
    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;
    private final ApplicationContext context;

    public AuthReaderService(AuthenticationManager authenticationManager,
            ReaderRepository readerRepository,
            ReaderMapper readerMapper,
            EmailService emailService,
            JwtService jwtService, TokenBlacklistService tokenBlacklistService, ApplicationContext context) {
        this.authenticationManager = authenticationManager;
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.context = context;
    }

    public RegisterReaderDtoResponse registerReader(RegisterReaderDtoRequest registerReaderDtoRequest) {
        var readerUsername = registerReaderDtoRequest.username();
        Boolean isReaderExists = readerRepository.existsByUsername(readerUsername);

        if (isReaderExists) {
            throw new ExistsException("You are trying to register a user who is already registered");
        }

        var newReaderEntity = readerMapper.toEntity(registerReaderDtoRequest);

        newReaderEntity.setPassword(encoder.encode(registerReaderDtoRequest.password()));
        newReaderEntity.setVerificationCode(GenerateVerificationCode.generateCode());
        newReaderEntity.setVerificationExpiration(LocalDateTime.now().plusMinutes(10));
        newReaderEntity.setIsEnabled(false);

        readerRepository.save(newReaderEntity);

        sendOTAC(newReaderEntity);

        return readerMapper.toDto(newReaderEntity);
    }

    public String verifyReader(VerifyReaderDtoRequest verifyReaderDtoRequest) {
        var readerEmail = verifyReaderDtoRequest.email();
        Reader neededReader = readerRepository.findByEmail(readerEmail);

        if (neededReader == null) {
            throw new NotFoundException("You are trying to verify a user who doesn't even exist!");
        }

        if (neededReader.getIsEnabled()) {
            throw new ExistsException("You are trying to verify a user who is already verified!");
        }

        if (neededReader.getVerificationExpiration().isBefore(LocalDateTime.now())) {
            throw new InvalidInputsException("The verification code has expired!");
        }

        if (!neededReader.getVerificationCode().equals(verifyReaderDtoRequest.verificationCode())) {
            throw new InvalidInputsException("Invalid verification code!");
        }

        neededReader.setIsEnabled(true);
        neededReader.setVerificationCode(null);
        neededReader.setVerificationExpiration(null);

        readerRepository.save(neededReader);

        return "User verified successfully! Login to receive your JWT token...";
    }

    public LoginReaderDtoResponse loginReader(LoginReaderDtoRequest loginReaderDtoRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginReaderDtoRequest.username(),
                    loginReaderDtoRequest.password()
            ));

            if (!authentication.isAuthenticated()) {
                return null;
            }

            String jwtToken = jwtService.generateToken(loginReaderDtoRequest);
            Long jwtExpirationMs = jwtService.getExpirationTime();
            String jwtExpirationText = "In " + String.valueOf(jwtService.getExpirationTime() / (1000 * 60 * 60)) + " Hours";


            return new LoginReaderDtoResponse(jwtToken, jwtExpirationMs, jwtExpirationText);
        } catch (BadCredentialsException e) {
            throw new InvalidInputsException("Invalid credentials! Please check your username and password.");
        }
    }

    public String logoutReader(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);


            if (token != null && jwtService.isTokenValid(token, userDetails)) {
                // Add token to blacklist
                tokenBlacklistService.blacklistToken(token);
                // Optional: Log the logout event
                logger.info("User {} logged out successfully", username);
            }

            return "Logged out successfully";

        } catch (Exception e) {
            // Even if something fails, return success (security best practice)
            return "Logged out successfully";
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void sendOTAC(Reader reader) {
        String verificationCode = reader.getVerificationCode();
        try {
            emailService.sendVerificationEmail(reader.getEmail(), verificationCode);
        } catch (MessagingException e) {
            logger.error("Error sending the email while signing up! {}", e.getMessage());
        }
    }

    public String reSendOTAC(String email) {
        Reader neededReader = readerRepository.findByEmail(email);
        if (neededReader == null) {
            throw new NotFoundException("You are trying to resend the OTAC to a user who doesn't even exist!");
        }
        if (neededReader.getIsEnabled()) {
            throw new InvalidInputsException("You are trying to resend the OTAC to a user who is already verified!");
        }

        String newVerificationOTAC = GenerateVerificationCode.generateCode();

        neededReader.setVerificationCode(newVerificationOTAC);
        neededReader.setVerificationExpiration(LocalDateTime.now().plusMinutes(10));

        readerRepository.save(neededReader);

        try {
            emailService.sendVerificationEmail(neededReader.getEmail(), newVerificationOTAC);
        } catch (MessagingException e) {
            logger.error("Error resending email while verifying! {}", e.getMessage());
        }

        return "The verification code was successfully resent!";
    }
}
