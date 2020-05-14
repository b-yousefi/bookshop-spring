package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
    Page<Order> findAllByUser_Id(Long userId, Pageable pageable);

    Page<Order> findAllByPlacedAt(Date placedAt, Pageable pageable);

    int countByUser_Id(Long userId);
}
