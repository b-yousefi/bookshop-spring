package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by: b.yousefi
 * Date: 5/14/2020
 */
@DataJpaTest
public class OrderItemRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    private OrderItem orderItem;

    @BeforeEach
    public void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderItem = ModelFactory.createOrderItem(entityManager);
    }

    @Test
    void findAllByOrder_Id() {
        Pageable sortedById =
                PageRequest.of(0, 3, Sort.by("id"));
        assertThat(orderItemRepository.findAllByOrder_Id(orderItem.getOrder().getId(), sortedById)).hasSize(1);
    }

    @Test
    void countByBook_Id() {
        assertThat(orderItemRepository.countByBook_Id(orderItem.getBook().getId())).isEqualTo(1);
    }

    @Test
    void countByOrder_Id() {
        assertThat(orderItemRepository.countByOrder_Id(orderItem.getOrder().getId())).isEqualTo(1);
    }
}
