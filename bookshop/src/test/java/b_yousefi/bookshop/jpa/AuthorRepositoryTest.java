package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.Author;
import b_yousefi.bookshop.models.DBFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */
@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private DBFileRepository dbFileRepository;

    private Author author;

    @AfterEach
    public void cleanup() {
        authorRepository.deleteAll();
        dbFileRepository.deleteAll();
    }

    @BeforeEach
    private void setup() {
        // given
        DBFile dbFile = ModelFactory.createDBFile(entityManager);
        author = ModelFactory.createAuthor(entityManager);
        if (dbFile != null) {
            author.setPicture(dbFile);
        }
        entityManager.persist(author);
    }

    @Test
    public void addingAuthorPicture() {
        Optional<Author> retAuthor = authorRepository.findById(author.getId());
        assertThat(retAuthor).isPresent();
        assertThat(dbFileRepository.findById(author.getPicture().getId())).isPresent();
    }

    @Test
    public void pictureRemovedAfterAuthorRemoved() {
        entityManager.remove(author);
        Optional<Author> retAuthor = authorRepository.findById(author.getId());
        assertThat(retAuthor).isNotPresent();
        assertThat(dbFileRepository.findById(author.getPicture().getId())).isNotPresent();
    }

    @Test
    public void saveAndFindByName() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("fullName"));
        assertThat(authorRepository.findAuthorsByFullNameContainingIgnoreCase("austen", sortedByName)).hasSize(1);
    }

}
