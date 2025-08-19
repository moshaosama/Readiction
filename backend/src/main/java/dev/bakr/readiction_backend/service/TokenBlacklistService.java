package dev.bakr.readiction_backend.service;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    // Thread-safe set to store blacklisted tokens (to handle concurrent operation when multiple users sign out simultaneously)
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final JwtService jwtService;

    public TokenBlacklistService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
        // Optional: Schedule automatic removal when token expires
        scheduleTokenCleanup(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    private void scheduleTokenCleanup(String blackListedToken) {
        try {
            // Get token expiration time
            Date expiration = jwtService.extractExpiration(blackListedToken);
            long delayMs = expiration.getTime() - System.currentTimeMillis();

            if (delayMs > 0) {
                /* Delay the garbage collecting until the JWT expires naturally, and do it automatically. Gives you an
                executor that waits delayMs before running the task (removing the expired token from the list). */
                CompletableFuture.delayedExecutor(delayMs, TimeUnit.MILLISECONDS)
                        .execute(() -> {
                            blacklistedTokens.remove(blackListedToken);
                            System.out.println("Removed expired token from blacklist");
                        });
            }
        } catch (Exception e) {
            /* If we can't parse expiration (Maybe the token was malformed or had no exp claim.), so schedule cleanup 24
            hours later just to avoid memory leaks. */
            CompletableFuture.delayedExecutor(24, TimeUnit.HOURS)
                    .execute(() -> blacklistedTokens.remove(blackListedToken));
        }
    }

    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }

    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}
