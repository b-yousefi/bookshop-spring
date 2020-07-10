package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book name is required")
    private String name;

    private Date publishedDay;

    @Builder.Default
    @ManyToMany(targetEntity = Author.class)
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOK__AUTHOR")),
            inverseJoinColumns = @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "FK_AUTHOR__BOOK")))
    private List<Author> authors = new ArrayList<>();

    @NotNull(message = "Each book must have a publication")
    @ManyToOne(targetEntity = Publication.class)
    @JoinColumn(name = "publication_id", foreignKey = @ForeignKey(name = "FK_PUBLICATION__BOOK"))
    private Publication publication;

    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "ISBN format is incorrect")
    private String ISBN;

    @Lob
    @Size(max = 100000, message = "Summary length cannot be more than 100000 characters")
    @Column(length = 100000)
    private String summary;

    @ManyToMany(targetEntity = Category.class)
    @JoinTable(name = "book_category", joinColumns = @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOK__CATEGORY")),
            inverseJoinColumns = @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_CATEGORY__BOOK")))
    @Size(min = 1, message = "Each book must have at least one category")
    private Set<Category> categories;

    @OneToOne(targetEntity = DBFile.class, cascade = ALL)
    @JoinColumn(name = "picture_id", foreignKey = @ForeignKey(name = "FK_PICTURE__BOOK"))
    private DBFile picture;

    @Min(value = 0, message = "Book price cannot be less than zero")
    private BigDecimal price;

    @Builder.Default
    @Min(value = 0, message = "Book Quantity cannot be less than zero")
    private int quantity = 1;

    public void putOrder(int orderedQuantity) {
        this.quantity -= orderedQuantity;
    }
}
