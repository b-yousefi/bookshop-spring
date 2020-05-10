package b_yousefi.bookshop.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Book name is required")
    private String name;
    private Date publishedDay;

    @ManyToMany(targetEntity = Author.class)
    private List<Author> authors = new ArrayList<>();

    @ManyToOne(targetEntity = Publication.class)
    private Publication publication;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "ISBN format is incorrect")
    private String ISBN;

    @ManyToMany(targetEntity = Category.class)
    private List<Category> categories;
}
