package dev.bakr.readiction_backend.responses;

public record WordDtoResponse(Long id,
        String wordContent,
        String translation,
        String relatedSentence,
        Integer pageNumber) {
}
