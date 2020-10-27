package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.OrderStatusRecord;
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
 * Date: 7/7/2020
 */
@RepositoryRestResource(collectionResourceRel = "order_statuses", path = "order_statuses")
public interface OrderStatusRecordRepository extends CrudRepository<OrderStatusRecord, Long> {

    @PreAuthorize("isAuthenticated()")
    @PostFilter("hasRole('ADMIN') || filterObject.order.user.username == principal.username")
    @Override
    Iterable<OrderStatusRecord> findAll();

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().order.user.username == principal.username))")
    @Override
    Optional<OrderStatusRecord> findById(Long id);

    @RestResource(path = "find_order_status_by_order_Id", rel = "find_order_status_by_order_Id")
    List<OrderStatusRecord> findAllByOrder_IdOrderByUpdatedAtDesc(@Param("orderId") Long orderId);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#s.order.user.username == principal.username))")
    @Override
    <S extends OrderStatusRecord> S save(S s);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#orderStatusRecord.order.user.username == principal.username))")
    @Override
    void delete(OrderStatusRecord orderStatusRecord);
}
