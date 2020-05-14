package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.Author;
import b_yousefi.bookshop.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    private Book book;

    @BeforeEach
    private void setUp() {
        bookRepository.deleteAll();
        book = ModelFactory.createBook(entityManager);
    }

    @Test
    public void validate_notBlank_name() {
        book.setName("");
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        // execute validation, check for violations
        Set<ConstraintViolation<Book>> violations =
                validator.validate(book, Default.class);
        // do we have one?
        assertThat(violations.size()).isEqualTo(1);
        // Assert.assertEquals(1, violations.size());

        // now, check the constraint violations to check for our specific error
        ConstraintViolation<Book> violation = violations.iterator().next();

        // contains the right message?
        assertThat("Book name is required").isEqualTo(
                violation.getMessageTemplate());

        // from the right field?
        assertThat("name").isEqualTo(
                violation.getPropertyPath().toString());
    }

    @Test
    public void validate_notBlank_ISBN() {
        book.setISBN("");
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        // execute validation, check for violations
        Set<ConstraintViolation<Book>> violations =
                validator.validate(book, Default.class);
        // do we have one?
        assertThat(violations.size()).isEqualTo(1);
        // Assert.assertEquals(1, violations.size());

        // now, check the constraint violations to check for our specific error
        ConstraintViolation<Book> violation = violations.iterator().next();

        // contains the right message?
        assertThat("ISBN format is incorrect").isEqualTo(
                violation.getMessageTemplate());

        // from the right field?
        assertThat("ISBN").isEqualTo(
                violation.getPropertyPath().toString());
    }

    @Test
    public void validate_pattern_ISBN() {
        book.setISBN("019280238");
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        // execute validation, check for violations
        Set<ConstraintViolation<Book>> violations =
                validator.validate(book, Default.class);
        // do we have one?
        assertThat(violations.size()).isEqualTo(1);
        // Assert.assertEquals(1, violations.size());

        // now, check the constraint violations to check for our specific error
        ConstraintViolation<Book> violation = violations.iterator().next();

        // contains the right message?
        assertThat("ISBN format is incorrect").isEqualTo(
                violation.getMessageTemplate());

        // from the right field?
        assertThat("ISBN").isEqualTo(
                violation.getPropertyPath().toString());
    }


    @Test
    public void findByAuthors_Id() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("name"));
        assertThat(bookRepository.findByAuthors_Id(book.getAuthors().iterator().next().getId(), sortedByName)).hasSize(1);
    }

    @Test
    public void findAllByPublication_Id() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("name"));
        assertThat(bookRepository.findAllByPublication_Id(book.getPublication().getId(), sortedByName)).hasSize(1);
    }

    @Test
    public void findByCategories_Id() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("name"));
        assertThat(bookRepository.findByCategories_Id(book.getCategories().iterator().next().getId(), sortedByName)).hasSize(1);
    }

    @Test
    public void add_two_authors_cascade() {
        Author author2 = Author.builder()
                .fullName("J. K. Rowling2")
                .build();
        entityManager.persist(author2);
        book.getAuthors().add(author2);
        entityManager.persist(book);
        assertThat(bookRepository.findById(book.getId())).isPresent();
        assertThat(bookRepository.findById(book.getId()).get().getAuthors()).hasSize(2);
    }

    @Test
    public void remove_author() {
        Author author2 = Author.builder()
                .fullName("J. K. Rowling2")
                .build();
        entityManager.persistAndFlush(author2);
        book.getAuthors().add(author2);
        entityManager.persistAndFlush(book);
        entityManager.refresh(author2);
        assertThat(book.getAuthors()).hasSize(2);
        assertThat(authorRepository.findAll()).hasSize(2);
        book.getAuthors().remove(author2);
        entityManager.remove(author2);
        assertThat(authorRepository.findAll()).hasSize(1);
        assertThat(book.getAuthors()).hasSize(1);
    }

}
