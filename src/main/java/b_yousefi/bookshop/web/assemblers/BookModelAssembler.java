package b_yousefi.bookshop.web.assemblers;

import b_yousefi.bookshop.controllers.BookController;
import b_yousefi.bookshop.entities.Author;
import b_yousefi.bookshop.entities.Book;
import b_yousefi.bookshop.entities.Category;
import b_yousefi.bookshop.repositories.BookRepository;
import b_yousefi.bookshop.web.models.BookModel;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 6/27/2020
 */
@Component
public class BookModelAssembler extends ModelAssembler<Book, BookModel> {

    public BookModelAssembler(RepositoryRestConfiguration config) {
        super(BookRepository.class, BookModel.class, config);
    }

    @Override
    public BookModel toModel(Book entity) {
        BookModel bookModel = instantiateModel(entity);

        bookModel.add(fixLinkSelf(
                methodOn(BookController.class)
                        .getBookById(entity.getId()))
                .withSelfRel());

        bookModel.setId(entity.getId());
        bookModel.setName(entity.getName());
        bookModel.setPublishedDay(entity.getPublishedDay());
        bookModel.setISBN(entity.getISBN());
        bookModel.setSummary(entity.getSummary());
        bookModel.setPrice(entity.getPrice());
        bookModel.setQuantity(entity.getQuantity());
        bookModel.setPicture(entity.getPicture());
        bookModel.setAuthorIds(entity.getAuthors().stream().map(Author::getId).collect(Collectors.toList()));
        bookModel.setPublicationId(entity.getPublication().getId());
        bookModel.setCategoryIds(entity.getCategories().stream().map(Category::getId).collect(Collectors.toList()));

        return bookModel;
    }

    @Override
    public CollectionModel<BookModel> toCollectionModel(Iterable<? extends Book> entities) {
        CollectionModel<BookModel> bookModels = super.toCollectionModel(entities);

        bookModels.add(fixLinkSelf(methodOn(BookController.class).getBooks()).withSelfRel());

        return bookModels;
    }
}
