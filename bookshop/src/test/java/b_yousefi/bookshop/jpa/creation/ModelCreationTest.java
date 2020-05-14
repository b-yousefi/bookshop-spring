package b_yousefi.bookshop.jpa.creation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/15/2020
 */
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ModelCreationTest {
    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testCreateDBFile() {
        assertThat(ModelFactory.createDBFile(entityManager)).isNotNull();
    }

    @Test
    public void testCreateUser() {
        assertThat(ModelFactory.createUser(entityManager)).isNotNull();
    }

    @Test
    public void testCreateAddress() {
        assertThat(ModelFactory.createAddress(entityManager)).isNotNull();
    }

    @Test
    public void testCreateAuthor() {
        assertThat(ModelFactory.createAuthor(entityManager)).isNotNull();
    }

    @Test
    public void testCreateCategory() {
        assertThat(ModelFactory.createCategory(entityManager)).isNotNull();
    }

    @Test
    public void testCreatePublication() {
        assertThat(ModelFactory.createPublication(entityManager)).isNotNull();
    }

    @Test
    public void testCreateBook() {
        assertThat(ModelFactory.createBook(entityManager)).isNotNull();
    }

    @Test
    public void testCreateOrder() {
        assertThat(ModelFactory.createOrder(entityManager)).isNotNull();
    }

    @Test
    public void testCreateOrderItem() {
        assertThat(ModelFactory.createOrderItem(entityManager)).isNotNull();
    }
}
