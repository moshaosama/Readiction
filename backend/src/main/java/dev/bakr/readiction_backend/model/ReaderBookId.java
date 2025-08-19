package dev.bakr.readiction_backend.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderBookId implements Serializable {
    private Long readerId;
    private Long bookId;

    /* Without overriding equals() and hashCode(), JPA/Hibernate will have a hard time correctly identifying and managing
    the entity in its persistence context (JPA’s workbench). Hibernate relies on equals() and hashCode() to know if two entity instances
    represent the same database row.*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReaderBookId that = (ReaderBookId) o;
        return Objects.equals(readerId, that.readerId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readerId, bookId);
    }
}
