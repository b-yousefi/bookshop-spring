package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Category;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */
public class CategoryRepositoryTest extends DataTest {
    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    public void setup() {
        category1 = Category.builder()
                .name("Fiction")
                .description("Fiction books contain a made-up story – a story that did not actually happen in real life. " +
                        "These stories are derived from the imagination and creativity of the authors and are not based on facts")
                .build();
        getEntityManager().persistAndFlush(category1);
        category2 = Category.builder()
                .name("Fairy Tale")
                .description("Fairy tale is usually a story for children that involves imaginary creatures and magical events.")
                .build();
        getEntityManager().persistAndFlush(category2);
        category3 = Category.builder()
                .name("Mythology")
                .description("These books include a legend or traditional narrative, often based in part on historical events, " +
                        "that reveals human behavior and natural phenomena by its symbolism and often pertaining to the actions of the gods.")
                .build();
    }

    @Test
    public void saveWithParent() {
        category2.setParentCat(category1);
        assertThat(getEntityManager().persistAndGetId(category2)).isNotNull();
    }

    @Test
    public void findWithParent() {
        category2.setParentCat(category1);
        getEntityManager().persist(category2);
        category3.setParentCat(category1);
        getEntityManager().persistAndFlush(category3);
        assertThat(getCategoryRepository().findAllByParentCat_Id(category1.getId())).hasSize(2);
    }

    @Test
    public void removeParent() {
        category2.setParentCat(category1);
        getEntityManager().persist(category2);
        category3.setParentCat(category1);
        getEntityManager().persistAndFlush(category3);
        getEntityManager().remove(category1);
        assertThat(getCategoryRepository().findAll()).hasSize(0);
    }

    @Test
    void testUniqueNameInEachCategory() {
        category2.setParentCat(category1);
        getEntityManager().persist(category2);
        category3.setParentCat(category1);
        category3.setName(category2.getName());
        PersistenceException exception = assertThrows(PersistenceException.class, () ->
                getEntityManager().persistAndFlush(category3));
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }
}
