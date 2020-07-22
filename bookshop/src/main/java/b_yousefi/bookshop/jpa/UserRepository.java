package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
    @PostFilter("hasRole('ADMIN') || filterObject.username == principal.username")
    @Override
    Iterable<User> findAll();

    @RestResource(exported = false)
    Optional<User> findByUsername(@Param("username") String username);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#username == principal.username)) ")
    @Query("select user from User user where  :username = user.username ")
    User findUser(@Param("username") String username);

    @PreAuthorize("hasRole('ADMIN') || (isAuthenticated() && (#s.username == principal.username)) || #s.id == null")
    @Override
    <S extends User> S save(S s);

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("hasRole('ADMIN') " +
            "|| (returnObject.isPresent() && (returnObject.get().username == principal.username))")
    @Override
    Optional<User> findById(Long id);
}