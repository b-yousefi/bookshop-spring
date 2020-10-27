package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/14/2020
 */
public class OrderItemRepositoryTest extends DataTest {

    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        orderItem = createOrderItem();
    }

    @Test
    void findAllByOrder_Id() {
        assertThat(getOrderItemRepository().findAllByOrder_Id(orderItem.getOrder().getId())).hasSize(1);
    }

    @Test
    void countByBook_Id() {
        assertThat(getOrderItemRepository().countByBook_Id(orderItem.getBook().getId())).isEqualTo(1);
    }

    @Test
    void countByOrder_Id() {
        assertThat(getOrderItemRepository().countByOrder_Id(orderItem.getOrder().getId())).isEqualTo(1);
    }

    @Test
    void getTotalPrice() {
        assertThat(getOrderRepository().getTotalPrice(orderItem.getOrder().getId()))
                .usingComparator(BigDecimal::compareTo).isEqualTo(new BigDecimal(250));
    }
}
