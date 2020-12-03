package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.Author;
import b_yousefi.bookshop.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by: b.yousefi Date: 5/13/2020
 */
public class BookRepositoryTest extends DataTest {
        private Book book;

        @BeforeEach
        public void setUp() {
                book = createBook();
        }

        @Test
        public void validate_notBlank_name() {
                book.setName("");
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();
                // execute validation, check for violations
                Set<ConstraintViolation<Book>> violations = validator.validate(book, Default.class);
                // do we have one?
                assertThat(violations.size()).isEqualTo(1);
                // Assert.assertEquals(1, violations.size());

                // now, check the constraint violations to check for our specific error
                ConstraintViolation<Book> violation = violations.iterator().next();

                // contains the right message?
                assertThat(violation.getMessageTemplate()).isEqualTo("Book name is required");

                // from the right field?
                String field = violation.getPropertyPath().toString();
                assertThat(field).isEqualTo("name");
                validator.close();
        }

        @Test
        public void validate_notBlank_ISBN() {
                book.setISBN("");
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();
                // execute validation, check for violations
                Set<ConstraintViolation<Book>> violations = validator.validate(book, Default.class);
                // do we have one?
                assertThat(violations.size()).isEqualTo(1);
                // Assert.assertEquals(1, violations.size());

                // now, check the constraint violations to check for our specific error
                ConstraintViolation<Book> violation = violations.iterator().next();

                // contains the right message?
                assertThat(violation.getMessageTemplate()).isEqualTo("ISBN format is incorrect");

                // from the right field?
                String field = violation.getPropertyPath().toString();
                assertThat(field).isEqualTo("ISBN");
                validator.close();
        }

        @Test
        public void validate_pattern_ISBN() {
                book.setISBN("019280238");
                LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
                validator.afterPropertiesSet();
                // execute validation, check for violations
                Set<ConstraintViolation<Book>> violations = validator.validate(book, Default.class);
                // do we have one?
                assertThat(violations.size()).isEqualTo(1);
                // Assert.assertEquals(1, violations.size());

                // now, check the constraint violations to check for our specific error
                ConstraintViolation<Book> violation = violations.iterator().next();

                // contains the right message?
                assertThat(violation.getMessageTemplate()).isEqualTo("ISBN format is incorrect");

                // from the right field?
                String field = violation.getPropertyPath().toString();
                assertThat(field).isEqualTo("ISBN");
                validator.close();
        }

        @Test
        public void findByAuthors_Id() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertThat(getBookRepository().findByAuthors_Id(book.getAuthors().iterator().next().getId(),
                                sortedByName)).hasSize(1);
        }

        @Test
        public void findAllByPublication_Id() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertThat(getBookRepository().findAllByPublication_Id(book.getPublication().getId(), sortedByName))
                                .hasSize(1);
        }

        @Test
        public void findByCategories_Id() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertTrue(book.getCategories().iterator().hasNext());
                assertThat(getBookRepository().findByCategories_Id(book.getCategories().iterator().next().getId(),
                                sortedByName)).hasSize(1);
        }

        @Test
        void findByPublication_Id() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertThat(getBookRepository().findByPublication_Ids(List.of(book.getPublication().getId()),
                                sortedByName)).hasSize(1);
        }

        @Test
        void findByPublication_Id_when_null_return_all() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertThat(getBookRepository().findByPublication_Ids(null, sortedByName)).hasSize(1);
        }

        @Sql("books_filter_data.sql")
        @Test
        void findByAuthor_Ids() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));
                assertThat(getBookRepository().findByAuthor_Ids(List.of(1L, 2L), sortedByName)).hasSize(4);
        }

        @Test
        void findByAuthor_Ids_when_null_return_all() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertTrue(book.getAuthors().iterator().hasNext());
                assertThat(getBookRepository().findByAuthor_Ids(null, sortedByName)).hasSize(1);
        }

        @Test
        void findByCategory_Ids() {
                Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
                assertTrue(book.getCategories().iterator().hasNext());
                assertThat(getBookRepository().findByCategory_Ids(
                                List.of(book.getCategories().iterator().next().getId()), sortedByName)).hasSize(1);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_when_params_are_null_return_all() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(null, null, null, sortedByName)).hasSize(5);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_when_publication() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(List.of(2L, 1L), null, null, sortedByName)).hasSize(4);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_with_authors_and_publication() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(List.of(2L), null, List.of(1L), sortedByName)).hasSize(1);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_with_authors_and_categories() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(List.of(1L), List.of(3L), null, sortedByName)).hasSize(1);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_with_authors_and_authors_category() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(null, List.of(2L), List.of(1L, 2L), sortedByName)).hasSize(2);
        }

        @Disabled("It does not work with h2 database!")
        @Sql("books_filter_data.sql")
        @Test
        void filter_with_authors_and_publication_category() {
                Pageable sortedByName = PageRequest.of(0, 10, Sort.by("name"));

                assertThat(getBookRepository().filter(List.of(1L), List.of(2L, 4L), List.of(1L, 2L), sortedByName))
                                .hasSize(2);
        }

        @Test
        public void add_with_no_name_then_throw_error() {
                // book must have a name
                Book book = Book.builder().publication(createPublication()).ISBN("0192802380").build();
                assertThrows(ConstraintViolationException.class, () -> getEntityManager().persist(book));
        }

        @Test
        public void add_with_publication_null_then_throw_error() {
                // book must have a publication
                Book book = Book.builder().name("The Alchemist").ISBN("0192802380").build();
                assertThrows(ConstraintViolationException.class, () -> getEntityManager().persist(book));
        }

        @Test
        public void add_two_authors_cascade() {
                Author author2 = Author.builder().fullName("J. K. Rowling2").build();
                getEntityManager().persist(author2);
                book.getAuthors().add(author2);
                getEntityManager().persist(book);
                assertThat(getBookRepository().findById(book.getId())).isPresent();
                assertThat(getBookRepository().findById(book.getId()).get().getAuthors()).hasSize(2);
        }

        @Test
        public void remove_author() {
                Author author2 = Author.builder().fullName("J. K. Rowling2").build();
                getEntityManager().persistAndFlush(author2);
                book.getAuthors().add(author2);
                getEntityManager().persistAndFlush(book);
                getEntityManager().refresh(author2);
                assertThat(book.getAuthors()).hasSize(2);
                assertThat(getAuthorRepository().findAll()).hasSize(2);
                book.getAuthors().remove(author2);
                getEntityManager().remove(author2);
                assertThat(getAuthorRepository().findAll()).hasSize(1);
                assertThat(book.getAuthors()).hasSize(1);
        }

        @Test
        public void remove_book() {
                assertThat(getBookRepository().findAll()).hasSize(1);
                assertThat(getPublicationRepository().findAll()).hasSize(1);
                assertThat(getAuthorRepository().findAll()).hasSize(1);
                assertThat(getCategoryRepository().findAll()).hasSize(1);
                getEntityManager().remove(book);
                assertThat(getBookRepository().findAll()).isEmpty();
                assertThat(getPublicationRepository().findAll()).hasSize(1);
                assertThat(getAuthorRepository().findAll()).hasSize(1);
                assertThat(getCategoryRepository().findAll()).hasSize(1);
        }

        @Test
        public void add_book_with_author() {
                assertThat(getBookRepository().findAll()).hasSize(1);
                assertThat(getAuthorRepository().findAll()).hasSize(1);
                Author author = Author.builder().fullName("Akhavan").build();
                getEntityManager().persistAndFlush(author);
                Book newBook = Book.builder().name("Winter").ISBN("1234567892").publication(book.getPublication())
                                .authors(List.of(author)).build();
                getEntityManager().persistAndFlush(newBook);
                assertThat(getBookRepository().findAll()).hasSize(2);
                assertThat(getAuthorRepository().findAll()).hasSize(2);
                getEntityManager().refresh(author);
                assertThat(author.getBooks()).hasSize(1);
        }

}
