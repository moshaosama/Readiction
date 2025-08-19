package dev.bakr.readiction_backend.responses;

import java.time.LocalDateTime;

public record RegisterReaderDtoResponse(Long id,
        String username,
        String email,
        String password,
        Boolean isEnabled,
        String verificationCode,
        LocalDateTime verificationExpiration) {
}
