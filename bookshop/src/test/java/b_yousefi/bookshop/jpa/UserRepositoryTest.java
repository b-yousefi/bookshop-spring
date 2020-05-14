package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.DBFile;
import b_yousefi.bookshop.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by: b.yousefi
 * Date: 5/12/2020
 */
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DBFileRepository dbFileRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
        dbFileRepository.deleteAll();
    }


    @Test
    public void whenFindAll_thenReturnUserList() {
        ModelFactory.createUser(entityManager);
        Iterable<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(1);
    }

    @Test
    public void testPictureSaved() throws IOException {
        DBFile dbFile = ModelFactory.createDBFile(entityManager);

        User user = ModelFactory.createUser(entityManager);

        user.setPicture(dbFile);
        entityManager.persist(user);
        assertThat(dbFileRepository.findById(user.getPicture().getId())).isPresent();
    }

    @Test
    public void testPictureRemovedAfterUserRemoved() throws IOException {
        DBFile dbFile = ModelFactory.createDBFile(entityManager);
        User user = ModelFactory.createUser(entityManager);
        user.setPicture(dbFile);
        entityManager.persist(user);
        entityManager.remove(user);
        Optional<User> retUser = userRepository.findById(user.getId());
        assertThat(retUser).isNotPresent();
        assertThat(dbFileRepository.findById(user.getPicture().getId())).isNotPresent();
    }

}
