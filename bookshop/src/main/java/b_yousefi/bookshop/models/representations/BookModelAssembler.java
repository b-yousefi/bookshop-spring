package b_yousefi.bookshop.models.representations;

import b_yousefi.bookshop.controllers.BookController;
import b_yousefi.bookshop.jpa.BookRepository;
import b_yousefi.bookshop.models.Author;
import b_yousefi.bookshop.models.Book;
import b_yousefi.bookshop.models.Category;
import lombok.SneakyThrows;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 6/27/2020
 */
@Component
public class BookModelAssembler extends RepresentationModelAssemblerSupport<Book, BookModel> {

    private RepositoryRestConfiguration config;

    public BookModelAssembler(RepositoryRestConfiguration config) {
        super(BookRepository.class, BookModel.class);
        this.config = config;
    }

    private Link fixLinkSelf(Object invocationValue) {
        return fixLinkTo(invocationValue).withSelfRel();
    }

    @SneakyThrows
    private Link fixLinkTo(Object invocationValue) {
        UriComponentsBuilder uriComponentsBuilder = linkTo(invocationValue).toUriComponentsBuilder();
        URL url = new URL(uriComponentsBuilder.toUriString());
        uriComponentsBuilder.replacePath(config.getBasePath() + url.getPath());
        return new Link(uriComponentsBuilder.toUriString());
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
