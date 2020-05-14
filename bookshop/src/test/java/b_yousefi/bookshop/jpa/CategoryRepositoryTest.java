package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/13/2020
 */

@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;


    @BeforeEach
    @AfterEach
    public void cleanup() {
        categoryRepository.deleteAll();
    }


    @Test
    public void saveWithParent() {
        Category parent = Category.builder()
                .name("Fiction")
                .description("Fiction books contain a made-up story – a story that did not actually happen in real life. " +
                        "These stories are derived from the imagination and creativity of the authors and are not based on facts")
                .build();
        entityManager.persist(parent);
        Category category = Category.builder()
                .name("Fairy Tale")
                .description("Fairy tale is usually a story for children that involves imaginary creatures and magical events.")
                .parentCat(parent)
                .build();
        assertThat(entityManager.persistAndGetId(category)).isNotNull();
    }

    @Test
    public void findWithParent() {
        Category parent = Category.builder()
                .name("Fiction")
                .description("Fiction books contain a made-up story – a story that did not actually happen in real life. " +
                        "These stories are derived from the imagination and creativity of the authors and are not based on facts")
                .build();
        entityManager.persist(parent);
        Category category = Category.builder()
                .name("Fairy Tale")
                .description("Fairy tale is usually a story for children that involves imaginary creatures and magical events.")
                .parentCat(parent)
                .build();
        entityManager.persist(category);
        Category category2 = Category.builder()
                .name("Mythology")
                .description("These books include a legend or traditional narrative, often based in part on historical events, " +
                        "that reveals human behavior and natural phenomena by its symbolism and often pertaining to the actions of the gods.")
                .parentCat(parent)
                .build();
        entityManager.persist(category2);
        assertThat(categoryRepository.findAllByParentCat(parent)).hasSize(2);
    }

    @Test
    public void removeParent() {
        Category parent = Category.builder()
                .name("Fiction")
                .description("Fiction books contain a made-up story – a story that did not actually happen in real life. " +
                        "These stories are derived from the imagination and creativity of the authors and are not based on facts")
                .build();
        entityManager.persist(parent);
        Category category = Category.builder()
                .name("Fairy Tale")
                .description("Fairy tale is usually a story for children that involves imaginary creatures and magical events.")
                .parentCat(parent)
                .build();
        entityManager.persist(category);
        Category category2 = Category.builder()
                .name("Mythology")
                .description("These books include a legend or traditional narrative, often based in part on historical events, " +
                        "that reveals human behavior and natural phenomena by its symbolism and often pertaining to the actions of the gods.")
                .parentCat(parent)
                .build();
        entityManager.persist(category2);
        entityManager.remove(parent);
        assertThat(categoryRepository.findAll()).hasSize(0);
    }
}
