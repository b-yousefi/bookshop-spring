package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface PublicationRepository extends CrudRepository<Publication, Long> {
    Page<Publication> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
