package b_yousefi.bookshop.data;

import b_yousefi.bookshop.models.Publication;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
