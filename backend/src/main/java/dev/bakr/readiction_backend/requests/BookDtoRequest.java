package dev.bakr.readiction_backend.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BookDtoRequest(@NotBlank(message = "Title is required") String title,
        String subtitle,
        String description,
        @Pattern(regexp = "97[89][0-9]{10}", message = "ISBN must be a 13-digit number starting with 978 or 979")
        String isbn,
        @NotNull(message = "Pages count is required")
        @Min(value = 1, message = "Pages count must be at least 1")
        Integer pagesCount,
        @NotBlank(message = "Image link is required")
        String imageLink,
        String printingType,
        Integer publishingYear,
        @NotBlank(message = "Author full name is required")
        String authorFullName,
        @NotBlank(message = "Category name is required")
        String categoryName,
        @NotBlank(message = "Publisher name is required")
        String publisherName) {
}
