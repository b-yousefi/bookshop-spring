package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.Author;
import b_yousefi.bookshop.entities.DBFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */
public class AuthorRepositoryTest extends DataTest {

    private Author author;

    @BeforeEach
    public void setup() {
        // given
        DBFile dbFile = createDBFile();
        author = createAuthor();
        if (dbFile != null) {
            author.setPicture(dbFile);
        }
        getEntityManager().persist(author);
    }

    @Test
    public void addingAuthorPicture() {
        Optional<Author> retAuthor = getAuthorRepository().findById(author.getId());
        assertThat(retAuthor).isPresent();
        assertThat(getDbFileRepository().findById(author.getPicture().getId())).isPresent();
    }

    @Test
    public void pictureRemovedAfterAuthorRemoved() {
        getEntityManager().remove(author);
        Optional<Author> retAuthor = getAuthorRepository().findById(author.getId());
        assertThat(retAuthor).isNotPresent();
        assertThat(getDbFileRepository().findById(author.getPicture().getId())).isNotPresent();
    }

    @Test
    public void saveAndFindByName() {
        Pageable sortedByName =
                PageRequest.of(0, 3, Sort.by("fullName"));
        assertThat(getAuthorRepository().findAuthorsByFullNameContainingIgnoreCase("austen", sortedByName)).hasSize(1);
    }

}
