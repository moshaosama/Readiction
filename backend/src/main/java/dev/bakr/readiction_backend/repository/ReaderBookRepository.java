package dev.bakr.readiction_backend.repository;

import dev.bakr.readiction_backend.model.ReaderBook;
import dev.bakr.readiction_backend.model.ReaderBookId;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReaderBookRepository extends JpaRepository<ReaderBook, ReaderBookId> {
    @Query("SELECT COUNT(rb) FROM ReaderBook rb WHERE rb.book.id = :bookId")
    Long countReadersByBookId(@Param("bookId") Long bookId);

    @NonNull
    Optional<ReaderBook> findById(@NonNull ReaderBookId readerBookId);

    boolean existsById(@NonNull ReaderBookId readerBookId);
}
