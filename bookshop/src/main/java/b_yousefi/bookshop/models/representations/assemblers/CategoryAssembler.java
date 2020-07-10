package b_yousefi.bookshop.models.representations.assemblers;

import b_yousefi.bookshop.controllers.CategoryController;
import b_yousefi.bookshop.models.Category;
import b_yousefi.bookshop.models.representations.CategoryRep;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 6/9/2020
 */
@Component
public class CategoryAssembler extends RepresentationModelAssemblerSupport<Category, CategoryRep> {

    public CategoryAssembler() {
        super(CategoryController.class, CategoryRep.class);
    }

    @Override
    public CategoryRep toModel(Category entity) {
        return null;
    }

    @Override
    public CollectionModel<CategoryRep> toCollectionModel(Iterable<? extends Category> entities)
    {
        CollectionModel<CategoryRep> categoryReps = super.toCollectionModel(entities);

        categoryReps.add(linkTo(methodOn(CategoryController.class).fetchAll()).withSelfRel());

        return categoryReps;
    }

//
//    @Override
//    public CollectionModel<CategoryRep> toCollectionModel(Iterable<? extends Category> entities) {
//
//        List<CategoryRep> categoryReps = new ArrayList<>();
//        entities.forEach(cat -> {
//            categoryReps.add(new CategoryRep(cat));
//        });
//        return new CollectionModel<>(categoryReps);
//    }

    //    public CategoryAssembler() {
//        super(CategoryController.class, CategoryRep.class);
//    }
//    @Override
//    protected CategoryRep instantiateResource(Category category) {
//        return new CategoryRep(category);
//    }
//
//    @Override
//    public CategoryRep toResource(Category category) {
//        return createResourceWithId(category.getId(), category);
//    }
}
