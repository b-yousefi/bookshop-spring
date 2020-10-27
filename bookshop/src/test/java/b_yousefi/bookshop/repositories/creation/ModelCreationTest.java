package b_yousefi.bookshop.repositories.creation;

import b_yousefi.bookshop.repositories.DataTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/15/2020
 */
public class ModelCreationTest extends DataTest {

    @Test
    public void testCreateDBFile() {
        assertThat(createDBFile()).isNotNull();
    }

    @Test
    public void testCreateUser() {
        assertThat(createUser()).isNotNull();
    }

    @Test
    public void testCreateAddress() {
        assertThat(createAddress()).isNotNull();
    }

    @Test
    public void testCreateAuthor() {
        assertThat(createAuthor()).isNotNull();
    }

    @Test
    public void testCreateCategory() {
        assertThat(createCategory()).isNotNull();
    }

    @Test
    public void testCreatePublication() {
        assertThat(createPublication()).isNotNull();
    }

    @Test
    public void testCreateBook() {
        assertThat(createBook()).isNotNull();
    }

    @Test
    public void testCreateOrder() {
        assertThat(createOrder()).isNotNull();
    }

    @Test
    public void testCreateOrderItem() {
        assertThat(createOrderItem()).isNotNull();
    }
}
