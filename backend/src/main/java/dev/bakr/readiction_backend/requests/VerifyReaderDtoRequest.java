package dev.bakr.readiction_backend.requests;

import jakarta.validation.constraints.NotBlank;

public record VerifyReaderDtoRequest(@NotBlank(message = "The email of the reader is required!") String email,
        @NotBlank(message = "The verification code is required!") String verificationCode) {
}
