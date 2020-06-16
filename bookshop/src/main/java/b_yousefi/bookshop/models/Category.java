package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
        @UniqueConstraint(name = "UK_CATEGORY__name_parent", columnNames = {"name", "parent_cat_id"})
})
@ToString(exclude = "parentCat")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(name = "name")
    private String name;

    @Lob
    @Size(max = 100000, message = "Description length cannot be more than 100000 characters")
    @Column(length = 100000)
    private String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parent_cat_id", foreignKey = @ForeignKey(name = "FK_CATEGORY__CATEGORY"))
    private Category parentCat;


    @OneToMany(mappedBy = "parentCat", orphanRemoval = true)
    private List<Category> subCategories;
}
