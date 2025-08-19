package dev.bakr.readiction_backend.requests;

import jakarta.validation.constraints.NotBlank;

public record LoginReaderDtoRequest(@NotBlank(message = "The username of the reader is required!") String username,
        @NotBlank(message = "The password of the reader is required!") String password) {
}
