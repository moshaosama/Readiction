package dev.bakr.readiction_backend.repository;

import dev.bakr.readiction_backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByIsbn(String bookIsbn);
}
