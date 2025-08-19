package dev.bakr.readiction_backend.config;

import dev.bakr.readiction_backend.exceptions.InvalidInputsException;
import dev.bakr.readiction_backend.service.JwtService;
import dev.bakr.readiction_backend.service.MyUserDetailsService;
import dev.bakr.readiction_backend.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final ApplicationContext context;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtFilter(ApplicationContext context,
            HandlerExceptionResolver handlerExceptionResolver,
            JwtService jwtService,
            TokenBlacklistService tokenBlacklistService) {
        this.context = context;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token;
        String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);


        try {
            // Check if token is blacklisted before making the auth token (allowing the user to access the resource)
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                throw new InvalidInputsException(
                        "Token has been invalidated! Login again to receive a new token.");
            }

            username = jwtService.extractUsername(token);

            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && existingAuth == null) {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

                if (jwtService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
