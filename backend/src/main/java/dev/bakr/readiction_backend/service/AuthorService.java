package dev.bakr.readiction_backend.service;

import dev.bakr.readiction_backend.model.Author;
import dev.bakr.readiction_backend.repository.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findOrCreateAuthor(String authorFullName) {
        return authorRepository.findByFullName(authorFullName)
                .orElseGet(() -> {
                    Author author = new Author();
                    author.setFullName(authorFullName);
                    return authorRepository.save(author);
                });
    }
}
