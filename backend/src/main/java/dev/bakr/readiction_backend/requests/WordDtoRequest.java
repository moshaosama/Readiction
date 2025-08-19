package dev.bakr.readiction_backend.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WordDtoRequest(@NotBlank(message = "The word content is required!") String wordContent,
        @NotBlank(message = "The translation is required!") String translation,
        @NotBlank(message = "The related sentence is required!") String relatedSentence,
        @NotNull(message = "The page number is required!") @Min(1) Integer pageNumber) {
}
