package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    Page<OrderItem> findAllByOrder_Id(Long order_Id, Pageable pageable);

    int countByBook_Id(Long book_Id);

    int countByOrder_Id(Long order_Id);
}
