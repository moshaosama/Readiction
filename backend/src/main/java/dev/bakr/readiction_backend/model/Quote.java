package dev.bakr.readiction_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id", updatable = false)
    private Long id;

    @Column(name = "text", nullable = false, length = 2000)
    private String text;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @ManyToOne
    @JsonIgnore
    @JoinColumns({
            @JoinColumn(name = "reader_id", referencedColumnName = "reader_id"),
            @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    })
    private ReaderBook readerBook;

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", pageNumber=" + pageNumber +
                ", readerBook=" + readerBook +
                '}';
    }
}
