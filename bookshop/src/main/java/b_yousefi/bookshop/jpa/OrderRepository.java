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
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@CrossOrigin(origins = {"http://localhost:3000"})
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

    //    @Query(value = "SELECT distinct * FROM Book ord WHERE ord.user.id = :userId ")
    @Query("select ord from Order ord inner join ord.orderStatusRecords st_records WHERE ord.user.id = :userId and st_records.status= :orderStatus")
    List<Order> findOrderWithStatus(@Param("userId") Long userId, @Param("orderStatus") OrderStatus orderStatus);

    @Query("select ord from Order ord inner join ord.orderStatusRecords st_records WHERE ord.user.username = :username and st_records.status= :orderStatus")
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
}
