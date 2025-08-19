package dev.bakr.readiction_backend.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryDtoResponse(Long id, String name) {
}
