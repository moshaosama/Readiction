package dev.bakr.readiction_backend.responses;

//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record BookDtoResponse(Long id,
        String title,
        String subtitle,
        String description,
        String isbn,
        Integer pagesCount,
        String imageLink,
        String printingType,
        Integer publishingYear,
        AuthorDtoResponse author,
        CategoryDtoResponse category,
        PublisherDtoResponse publisher) {
}
