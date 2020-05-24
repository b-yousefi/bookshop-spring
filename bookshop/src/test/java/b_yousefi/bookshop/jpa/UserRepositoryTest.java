package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.DBFile;
import b_yousefi.bookshop.models.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by: b.yousefi
 * Date: 5/12/2020
 */
public class UserRepositoryTest extends DataTest {

    @Test
    public void whenFindAll_thenReturnUserList() {
        createUser();
        Iterable<User> users = getUserRepository().findAll();

        // then
        assertThat(users).hasSize(1);
    }

    @Test
    public void testPictureSaved() throws IOException {
        DBFile dbFile = createDBFile();

        User user = createUser();

        user.setPicture(dbFile);
        getEntityManager().persist(user);
        assertThat(getDbFileRepository().findById(user.getPicture().getId())).isPresent();
    }

    @Test
    public void testPictureRemovedAfterUserRemoved() throws IOException {
        DBFile dbFile = createDBFile();
        User user = createUser();
        user.setPicture(dbFile);
        getEntityManager().persist(user);
        getEntityManager().remove(user);
        Optional<User> retUser = getUserRepository().findById(user.getId());
        assertThat(retUser).isNotPresent();
        assertThat(getDbFileRepository().findById(user.getPicture().getId())).isNotPresent();
    }
}
