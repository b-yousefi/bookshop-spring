package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface AddressRepository extends CrudRepository<Address, Long> {

    @PostFilter("hasRole('ADMIN') || filterObject.user.username == principal.username")
    @Override
    Iterable<Address> findAll();

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#username == principal.username))")
    Page<Address> findAllByUser_username(@Param("username") String username, Pageable pageable);

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().user.username == principal.username))")
    @Override
    Optional<Address> findById(Long aLong);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#s.user.username == principal.username))")
    @Override
    <S extends Address> S save(S s);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#address.user.username == principal.username))")
    @Override
    void delete(Address address);
}
