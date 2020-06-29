package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@CrossOrigin(origins = {"http://localhost:3000"})
public interface PublicationRepository extends CrudRepository<Publication, Long> {
    @RestResource(rel = "publicationsByName", path = "publicationsByName")
    Page<Publication> findAllByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}
