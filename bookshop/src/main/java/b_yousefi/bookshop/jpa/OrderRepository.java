package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Order;
import b_yousefi.bookshop.models.OrderStatus;
import b_yousefi.bookshop.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    @Override
    List<Order> findAll();

    @PreAuthorize("isAuthenticated() && #user.username == principal.username")
    Page<Order> findAllByUser(@Param("user") User user, Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().user.username == principal.username))")
    @Override
    Optional<Order> findById(Long aLong);

    @Query(value = "select * " +
            "from order_table " +
            "where id " +
            "in ( " +
            "select order_status.order_id " +
            "from order_status " +
            "where (order_id, order_status.updated_at) " +
            "in " +
            "(select order_id, max(os.updated_at) as tiem " +
            "from order_table " +
            "inner join order_status os on order_table.id = os.order_id " +
            "inner join user u on order_table.user_id = u.id " +
            "where u.username = :username " +
            "group by os.order_id) " +
            "and order_status.status = :#{#orderStatus.getStatusCode()} " +
            ")", nativeQuery = true)
    List<Order> findOrderWithStatusAndUserName(@Param("username") String username, @Param("orderStatus") OrderStatus orderStatus);

    @RestResource(path = "myOrders", rel = "myOrders")
    @PreAuthorize("isAuthenticated() && (#username == principal.username)")
    Page<Order> findAllByUser_username(@Param("username") String username, Pageable pageable);

    @RestResource(exported = false)
    @PreAuthorize("hasRole('ADMIN')")
    int countByUser_Username(@Param("username") String username);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#s.user.username == principal.username))")
    @Override
    <S extends Order> S save(S s);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#order.user.username == principal.username))")
    @Override
    void delete(Order order);

    @RestResource(exported = false)
    @Query(value = "select sum(order_item.quantity * book.price) from order_item inner join book on order_item.book_id = book.id where order_id=:orderId", nativeQuery = true)
    BigDecimal getTotalPrice(@Param("orderId") Long orderId);
}
