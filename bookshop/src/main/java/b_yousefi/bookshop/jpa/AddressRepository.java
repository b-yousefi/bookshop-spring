package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface AddressRepository extends CrudRepository<Address, Long> {
    Page<Address> findByUser_Id(Long userId, Pageable pageable);
}
