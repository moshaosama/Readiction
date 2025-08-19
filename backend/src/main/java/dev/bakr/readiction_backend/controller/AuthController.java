package dev.bakr.readiction_backend.controller;

import dev.bakr.readiction_backend.requests.LoginReaderDtoRequest;
import dev.bakr.readiction_backend.requests.RegisterReaderDtoRequest;
import dev.bakr.readiction_backend.requests.VerifyReaderDtoRequest;
import dev.bakr.readiction_backend.responses.LoginReaderDtoResponse;
import dev.bakr.readiction_backend.responses.RegisterReaderDtoResponse;
import dev.bakr.readiction_backend.service.AuthReaderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthReaderService authReaderService;

    public AuthController(AuthReaderService authReaderService) {
        this.authReaderService = authReaderService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterReaderDtoResponse> registerReader(@Valid @RequestBody RegisterReaderDtoRequest registerReaderDtoRequest) {
        RegisterReaderDtoResponse registeredReader = authReaderService.registerReader(registerReaderDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredReader);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyReader(@Valid @RequestBody VerifyReaderDtoRequest verifyReaderDtoRequest) {
        String verificationMessage = authReaderService.verifyReader(verifyReaderDtoRequest);
        return ResponseEntity.ok(verificationMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginReaderDtoResponse> loginReader(@Valid @RequestBody LoginReaderDtoRequest loginReaderDtoRequest) {
        LoginReaderDtoResponse loginReaderDtoResponse = authReaderService.loginReader(loginReaderDtoRequest);
        return ResponseEntity.ok(loginReaderDtoResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutReader(HttpServletRequest request) {
        String loggedOutMessage = authReaderService.logoutReader(request);
        return ResponseEntity.ok(loggedOutMessage);
    }

    @PostMapping("/resend")
    public ResponseEntity<String> reSendOTAC(@RequestParam String email) {
        String resendingMessage = authReaderService.reSendOTAC(email);
        return ResponseEntity.ok(resendingMessage);
    }
}
