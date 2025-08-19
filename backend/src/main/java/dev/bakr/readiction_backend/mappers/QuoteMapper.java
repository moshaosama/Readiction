package dev.bakr.readiction_backend.mappers;

import dev.bakr.readiction_backend.model.Quote;
import dev.bakr.readiction_backend.requests.QuoteDtoRequest;
import dev.bakr.readiction_backend.responses.QuoteDtoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuoteMapper {
    Quote toEntity(QuoteDtoRequest quoteDtoRequest);

    QuoteDtoResponse toDto(Quote quote);
}
