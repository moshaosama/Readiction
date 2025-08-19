package dev.bakr.readiction_backend.responses;

public record LoginReaderDtoResponse(String jwtToken, Long jwtExpirationMs, String jwtExpirationText) {
}
