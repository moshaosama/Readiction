package dev.bakr.readiction_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id", updatable = false)
    private Long id;

    @Column(name = "word_content", nullable = false, length = 45)
    private String wordContent;

    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(name = "related_sentence", nullable = false, length = 2000)
    private String relatedSentence;

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
        return "Word{" +
                "id=" + id +
                ", wordContent='" + wordContent + '\'' +
                ", translation='" + translation + '\'' +
                ", relatedSentence='" + relatedSentence + '\'' +
                ", pageNumber=" + pageNumber +
                ", readerBook=" + readerBook +
                '}';
    }
}
