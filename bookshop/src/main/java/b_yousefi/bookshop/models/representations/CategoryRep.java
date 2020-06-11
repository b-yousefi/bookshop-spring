package b_yousefi.bookshop.models.representations;

import b_yousefi.bookshop.models.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 6/9/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "categories", itemRelation = "category")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRep extends RepresentationModel<CategoryRep> {
    private Long id;
    private String name;
    private String description;
    private List<CategoryRep> subCategories;

    public CategoryRep(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        subCategories = new ArrayList<>();
        category.getSubCategories().forEach(cat -> {
            subCategories.add(new CategoryRep(cat));
        });
    }
}
