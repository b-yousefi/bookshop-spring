package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Book name is required")
    private String name;
    private Date publishedDay;

    @ManyToMany(targetEntity = Author.class)
    @Size(min = 1, message = "Each book must have at least one author")
    private Set<Author> authors;

    @NonNull
    @ManyToOne(targetEntity = Publication.class, cascade = CascadeType.ALL)
    private Publication publication;

    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "ISBN format is incorrect")
    private String ISBN;
    @Lob
    @Column(length = 100000)
    private String summary;

    @ManyToMany(targetEntity = Category.class)
    @Size(min = 1, message = "Each book must have at least one category")
    private Set<Category> categories;
}
