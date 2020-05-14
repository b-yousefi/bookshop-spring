package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.jpa.creation.ModelFactory;
import b_yousefi.bookshop.models.Address;
import b_yousefi.bookshop.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AddressRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @AfterEach
    public void cleanup() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        Address address = ModelFactory.createAddress(entityManager);
        user = address.getUser();
    }

    @Test
    public void whenFindAll_thenReturnAddressList() {
        // when
        Iterable<Address> addresses = addressRepository.findAll();

        // then
        assertThat(addresses).hasSize(1);
    }

    @Test
    public void whenFindByUser_thenReturnAddressList() {
        Pageable sortedByZipCode =
                PageRequest.of(0, 3, Sort.by("zipCode"));
        assertThat(addressRepository.findByUser_Id(user.getId(), sortedByZipCode)).hasSize(1);
    }

    @Test
    public void whenUserIsRomovedItsAddressesRmoved() {
        entityManager.remove(user);
        assertThat(userRepository.findByUsername(user.getUsername())).isNull();
        Pageable sortedByZipCode =
                PageRequest.of(0, 3, Sort.by("zipCode"));
        assertThat(addressRepository.findAll()).hasSize(0);
        assertThat(addressRepository.findByUser_Id(user.getId(), sortedByZipCode)).hasSize(0);
    }
}