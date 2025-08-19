package dev.bakr.readiction_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/* This entity class makes the relationship between the book and reader instead of just using the generic join table.
Because we're going to store data based on the relationship between the book and the reader. The entity has two many-To
-one relationships two achieve the many-to-many between readers and the books*/
@Entity
@Table(name = "readers_books")
@Setter
@Getter
@NoArgsConstructor
public class ReaderBook {
    @EmbeddedId
//    @Column(name = "reader_book_id")
    private ReaderBookId id; // composite PK (reader_id + book_id)

    /* Many records (rows) of this entity can be associated with one row in the Reader table. One reader entity will
    appear in many rows here by its readerId */
    @ManyToOne
    @MapsId("readerId") // refers to the readerId from the composite PK (@EmbeddedId)
    /* Specifies the column in this table that will store the foreign key referencing the primary key of the Reader entity
    (reader_id column in the readers table). */
    @JoinColumn(name = "reader_id")
    private Reader reader;

    /* Many records (rows) of this entity can be associated with one row in the book table. One book entity will
    appear in many rows here by its bookId */
    @ManyToOne
    @MapsId("bookId") // refers to the bookId from the composite PK (@EmbeddedId)
    /* Specifies the column in this table that will store the foreign key referencing the primary key of the Book entity
    (book_id column in the books table). */
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "adding_date")
    private LocalDate addingDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "left_off_page")
    private Integer leftOffPage;

    @OneToMany(mappedBy = "readerBook")
    private List<Quote> quotes;

    @OneToMany(mappedBy = "readerBook")
    private List<Word> words;

    public ReaderBook(Reader reader, Book book) {
        // creating the composite PK (allowing @MapsId to extract from it)
        this.id = createCompositeKey(reader.getId(), book.getId());
        this.reader = reader;
        this.book = book;
        this.addingDate = LocalDate.now();
        this.status = "unread";
        this.leftOffPage = null;
        this.quotes = new ArrayList<>();
        this.words = new ArrayList<>();
    }

    public static ReaderBookId createCompositeKey(Long readerId, Long bookId) {
        return new ReaderBookId(readerId, bookId);
    }
}
