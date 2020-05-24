package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasRole('ADMIN') || filterObject.user.username == principal.username")
    @Override
    Iterable<Order> findAll();

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().user.username == principal.username))")
    @Override
    Optional<Order> findById(Long aLong);

    @RestResource(path = "myOrders", rel = "myOrders")
    @PreAuthorize("isAuthenticated() && (#username == principal.username)")
    Page<Order> findAllByUser_username(@Param("username") String username, Pageable pageable);

    @RestResource(path = "myOrdersRegisteredAtDate", rel = "myOrdersRegisteredAtDate")
    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#username == principal.username))")
    Page<Order> findAllByUser_UsernameAndPlacedAt(@Param("username") String username,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") @Param("orderDate") Date placedAt, Pageable pageable);

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
