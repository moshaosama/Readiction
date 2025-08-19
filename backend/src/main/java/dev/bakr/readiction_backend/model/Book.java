package dev.bakr.readiction_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false, unique = true, length = 1000)
    private String title;

    @Column(name = "subtitle", length = 3000)
    private String subtitle;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "isbn", nullable = false, unique = true, length = 13)
    private String isbn;

    @Column(name = "pages_count", nullable = false)
    private Integer pagesCount;

    @Column(name = "image_link", length = 2000)
    private String imageLink;

    @Column(name = "printing_type")
    private String printingType;

    @Column(name = "publishing_year")
    private Integer publishingYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pagesCount=" + pagesCount +
                ", imageLink='" + imageLink + '\'' +
                ", printingType='" + printingType + '\'' +
                ", publishingYear=" + publishingYear +
                ", author=" + author +
                ", category=" + category +
                ", publisher=" + publisher +
                '}';
    }
}
