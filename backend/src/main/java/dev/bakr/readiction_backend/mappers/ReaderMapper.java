package dev.bakr.readiction_backend.mappers;

import dev.bakr.readiction_backend.model.Reader;
import dev.bakr.readiction_backend.requests.RegisterReaderDtoRequest;
import dev.bakr.readiction_backend.responses.RegisterReaderDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReaderMapper {
    Reader toEntity(RegisterReaderDtoRequest registerReaderDtoRequest);

    RegisterReaderDtoResponse toDto(Reader reader);
}
