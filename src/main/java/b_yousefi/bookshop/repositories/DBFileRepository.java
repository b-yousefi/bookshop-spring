package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.DBFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@RepositoryRestResource(exported = false)
public interface DBFileRepository extends CrudRepository<DBFile, Long> {
}
