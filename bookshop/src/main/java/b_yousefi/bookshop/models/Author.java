package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by: b.yousefi Date: 5/10/2020
 */

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(exclude = "books")
@Table(uniqueConstraints = { @UniqueConstraint(name = "UK_AUTHOR__fullName", columnNames = { "fullName" }) })
public class Author {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "fullName is required")
    private String fullName;

    private Date birthday;

    @Lob
    @Size(max = 100000, message = "Description length cannot be more than 100000 characters")
    @Column(length = 100000)
    private String description;

    @OneToOne(targetEntity = DBFile.class, cascade = ALL)
    @JoinColumn(name = "picture_id", foreignKey = @ForeignKey(name = "FK_PICTURE__AUTHOR"))
    private DBFile picture;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    private List<Book> books = new ArrayList<>();

    @PreRemove
    public void removeFromBooks() {
        for (Book book : books) {
            book.getAuthors().remove(this);
        }
    }
}
