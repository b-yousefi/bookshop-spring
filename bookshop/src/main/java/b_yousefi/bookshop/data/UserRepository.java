package b_yousefi.bookshop.data;

import b_yousefi.bookshop.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}