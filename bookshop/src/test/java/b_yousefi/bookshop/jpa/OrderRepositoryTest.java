package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.Order;
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
public class OrderRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        order = ModelFactory.createOrder(entityManager);
    }

    @Test
    public void findAllByUser_Id() {
        Pageable sortedByPlacedAt =
                PageRequest.of(0, 3, Sort.by("placedAt"));
        assertThat(orderRepository.findAllByUser_Id(order.getUser().getId(), sortedByPlacedAt)).hasSize(1);
    }

    @Test
    public void findAllByPlacedAt() {
        Pageable sortedByUser =
                PageRequest.of(0, 3, Sort.by("user_Id"));
        assertThat(orderRepository.findAllByPlacedAt(order.getPlacedAt(), sortedByUser)).hasSize(1);
    }

    @Test
    void countByUser_Id() {
        assertThat(orderRepository.countByUser_Id(order.getUser().getId())).isEqualTo(1);
    }

}
