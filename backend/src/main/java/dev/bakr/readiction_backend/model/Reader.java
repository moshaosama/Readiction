package dev.bakr.readiction_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "readers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @Column(name = "verification_code", length = 6)
    private String verificationCode;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationExpiration;

    /* One Reader (parent) can be linked to many ReaderBook entities (children). 'mappedBy = "reader"' tells JPA that the
    ReaderBook entity owns the relationship via its 'reader' field. This list represents all ReaderBook entries for this
    Reader in the persistence context. */
    // cascade = CascadeType.All : Any operation you perform on the parent (Reader) will also apply to the children (ReaderBook)
    /* orphanRemoval = true : If you remove an element from the readerBooks list in memory and then save the Reader, Hibernate
    will also delete that ReaderBook row from the database. */
    @OneToMany(mappedBy = "reader")
    private List<ReaderBook> readerBooks = new ArrayList<>();

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isEnabled=" + isEnabled +
                ", verificationCode='" + verificationCode + '\'' +
                ", verificationExpiration=" + verificationExpiration +
                ", readerBooks=" + readerBooks +
                '}';
    }
}
