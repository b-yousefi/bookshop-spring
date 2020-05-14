package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Category name is required")
    private String name;
    @Lob
    @Column(length = 100000)
    private String description;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parentCat;
}
