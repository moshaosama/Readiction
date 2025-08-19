package dev.bakr.readiction_backend.requests;

import jakarta.validation.constraints.NotBlank;

public record ReaderBookDtoRequest(@NotBlank(message = "The book should have a status!") String status,
        Integer leftOffPage) {
}
