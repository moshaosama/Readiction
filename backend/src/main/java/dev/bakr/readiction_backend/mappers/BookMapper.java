package dev.bakr.readiction_backend.mappers;

import dev.bakr.readiction_backend.model.Book;
import dev.bakr.readiction_backend.requests.BookDtoRequest;
import dev.bakr.readiction_backend.responses.BookDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDtoResponse toDto(Book book);

    Book toEntity(BookDtoRequest bookDtoRequest);
}
