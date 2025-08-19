package dev.bakr.readiction_backend.repository;

import dev.bakr.readiction_backend.model.ReaderBookId;
import dev.bakr.readiction_backend.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT COUNT(w) > 0 FROM Word w WHERE w.wordContent = :wordContent AND w.readerBook.id = :readerBookId")
    boolean existsByWordContentAndReaderBookId(@Param("wordContent") String wordContent,
            @Param("readerBookId") ReaderBookId readerBookId);

    @Query("SELECT w FROM Word w WHERE w.id = :wordId AND w.readerBook.id = :readerBookId")
    Word findByIdAndReaderBookId(@Param("wordId") Long wordId, @Param("readerBookId") ReaderBookId readerBookId);
}
