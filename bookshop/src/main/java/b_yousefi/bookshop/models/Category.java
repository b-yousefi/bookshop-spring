package b_yousefi.bookshop.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Category name is required")
    private String name;
    private String description;

    @ManyToOne
    private Category parentCat;

    @OneToMany(mappedBy="parentCat")
    private List<Category> subCategories = new ArrayList<>();

    @ManyToMany(targetEntity = Book.class)
    private List<Book> books;
}
