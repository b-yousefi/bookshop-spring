package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "parent_cat_id"})
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Category name is required")
    @Column(name = "name")
    private String name;
    @Lob
    @Column(length = 100000)
    private String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_cat_id")
    private Category parentCat;


    @OneToMany(mappedBy = "parentCat", orphanRemoval = true)
    private List<Category> subCategories;
}
