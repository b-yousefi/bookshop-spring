package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.entities.Book;
import b_yousefi.bookshop.repositories.BookRepository;
import b_yousefi.bookshop.web.assemblers.BookModelAssembler;
import b_yousefi.bookshop.web.models.BookModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 6/27/2020
 */
@RepositoryRestController
public class BookController {

    private BookRepository bookRepository;
    private BookModelAssembler bookModelAssembler;
    private PagedResourcesAssembler<Book> pagedResourcesAssembler;

    @Autowired
    BookController(BookRepository bookRepository, BookModelAssembler bookModelAssembler,
                   PagedResourcesAssembler<Book> pagedResourcesAssembler) {
        this.bookRepository = bookRepository;
        this.bookModelAssembler = bookModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<BookModel>> getBooks() {
        return new ResponseEntity<>(
                bookModelAssembler.toCollectionModel(bookRepository.findAll()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/books/filter", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<BookModel>> filterBooks(
            @RequestParam(required = false) List<Long> publicationIds,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> authorIds,
            Pageable pageable) {
        Page<Book> bookEntities = bookRepository.filter(publicationIds, categoryIds, authorIds, pageable);
        PagedModel<BookModel> collModel = pagedResourcesAssembler
                .toModel(bookEntities, bookModelAssembler);
        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<BookModel> getBookById(@PathVariable("id") Long id) {
        return bookRepository.findById(id)
                .map(bookModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
