package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Author {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "fullName is required")
    @Column(unique = true)
    private String fullName;
    private Date birthday;
    @Lob
    @Column(length = 100000)
    private String description;
    @OneToOne(targetEntity = DBFile.class, cascade = ALL)
    private DBFile picture;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "authors")
//    private List<Book> books = new ArrayList<>();
//
//    @PreRemove
//    public void removeFromBooks() {
//        for (Book book : books) {
//            book.getAuthors().remove(this);
//        }
//    }
}
