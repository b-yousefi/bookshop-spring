package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.Address;
import b_yousefi.bookshop.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class AddressRepositoryTest extends DataTest {

    private User user;
    private Address address;

    @BeforeEach
    public void setUp() throws Exception {
        address = createAddress();
        getEntityManager().clear();
        user = getUserRepository().findByUsername(address.getUser().getUsername()).orElseThrow(Exception::new);
        address = getAddressRepository().findAll().iterator().next();
    }

    @Test
    public void whenFindAll_thenReturnAddressList() {
        // when
        Iterable<Address> addresses = getAddressRepository().findAll();

        // then
        assertThat(addresses).hasSize(1);
    }

    @Test
    public void whenFindByUser_thenReturnAddressList() {
        Pageable sortedByZipCode =
                PageRequest.of(0, 3, Sort.by("zipCode"));
        assertThat(getAddressRepository().findAllByUser_username(user.getUsername(), sortedByZipCode)).hasSize(1);
    }

    @Test
    public void when_address_is_removed_user_is_not_removed() {
        assertThat(getAddressRepository().findAll()).hasSize(1);
        assertThat(getUserRepository().findAll()).hasSize(1);
        getAddressRepository().delete(address);
        getEntityManager().flush();
        getEntityManager().clear();
        assertThat(getAddressRepository().findAll()).hasSize(0);
        assertThat(getUserRepository().findAll()).hasSize(1);
    }

    @Test
    public void whenUserIsRemovedItsAddressesRemoved() {
        getEntityManager().remove(user);
        getEntityManager().flush();
        assertThat(getUserRepository().findByUsername(user.getUsername())).isNotPresent();
        Pageable sortedByZipCode =
                PageRequest.of(0, 3, Sort.by("zipCode"));

        assertThat(getAddressRepository().findAll()).hasSize(0);
        assertThat(getAddressRepository().findAllByUser_username(user.getUsername(), sortedByZipCode)).hasSize(0);
    }
}