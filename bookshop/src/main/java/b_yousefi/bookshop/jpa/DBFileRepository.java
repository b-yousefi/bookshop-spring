package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.DBFile;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface DBFileRepository extends CrudRepository<DBFile, Long> {
}
