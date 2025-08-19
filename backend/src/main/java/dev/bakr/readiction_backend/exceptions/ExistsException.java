package dev.bakr.readiction_backend.exceptions;

public class ExistsException extends RuntimeException {
    public ExistsException(String message) {
        super(message);
    }
}
