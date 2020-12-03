package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@RepositoryRestResource(collectionResourceRel = "order_items", path = "order_items")
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasRole('ADMIN') || filterObject.order.user.username == principal.username")
    @Override
    Iterable<OrderItem> findAll();

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().order.user.username == principal.username))")
    @Override
    Optional<OrderItem> findById(Long aLong);

    @RestResource(path = "byOrderId", rel = "byOrderId")
    @PreAuthorize("isAuthenticated()")
    List<OrderItem> findAllByOrder_Id(@Param("orderId") Long orderId);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#s.order.user.username == principal.username))")
    @Override
    <S extends OrderItem> S save(S s);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#orderItem.order.user.username == principal.username))")
    @Override
    void delete(OrderItem orderItem);

    @PreAuthorize("hasRole('ADMIN')")
    int countByBook_Id(Long book_Id);

    @PreAuthorize("hasRole('ADMIN')")
    int countByOrder_Id(Long order_Id);
}
