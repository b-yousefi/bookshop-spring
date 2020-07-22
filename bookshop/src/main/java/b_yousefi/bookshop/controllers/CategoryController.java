package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.jpa.CategoryRepository;
import b_yousefi.bookshop.models.Category;
import b_yousefi.bookshop.models.representations.CategoryRep;
import b_yousefi.bookshop.models.representations.assemblers.CategoryAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 6/9/2020
 */
@RestController
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryAssembler categoryAssembler;

    @RequestMapping("/api/categories/allcategories")
    @ResponseBody
    public ResponseEntity<CollectionModel<CategoryRep>> fetchAll() {
        List<Category> categoryList = categoryRepository.findAllByParentCat_Id(null);
        List<CategoryRep> categories = categoryList.stream().map(CategoryRep::new).collect(Collectors.toList());
        CollectionModel<CategoryRep> resourceResponse = new CollectionModel<>(categories);
        resourceResponse.add(linkTo(methodOn(CategoryController.class).fetchAll()).withSelfRel());
        return new ResponseEntity<>(resourceResponse, HttpStatus.OK);
    }
}
