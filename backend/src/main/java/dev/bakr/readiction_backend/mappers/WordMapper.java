package dev.bakr.readiction_backend.mappers;

import dev.bakr.readiction_backend.model.Word;
import dev.bakr.readiction_backend.requests.WordDtoRequest;
import dev.bakr.readiction_backend.responses.WordDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordMapper {
    Word toEntity(WordDtoRequest wordDtoRequest);

    WordDtoResponse toDto(Word word);
}
