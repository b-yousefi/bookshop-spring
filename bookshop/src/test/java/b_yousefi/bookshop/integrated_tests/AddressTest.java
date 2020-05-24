package b_yousefi.bookshop.integrated_tests;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by: b.yousefi
 * Date: 5/22/2020
 */
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_address.sql")
        }
)
public class AddressTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + ADDRESSES_PATH_NAME;

    @Test
    void when_user_is_anonymous_then_get_is_FORBIDDEN() throws Exception {
        //anonymous users cannot get from addresses path
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_get_its_own_addresses() throws Exception {
        //users with USER role can get the filtered data, all the addresses that belong to them
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        //get the information for addresses with id = 2, which belongs to user with role USER, with its own credential
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //user with role USER cannot get other users addresses
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_get_any_user_addresses() throws Exception {
        //users with ADMIN role can get from addresses path
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME)).header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
        ;
    }

    @Test
    void when_user_with_role_USER_then_can_post_its_own_addresses() throws Exception {
        //there is one address for this user
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //add address to current user
        String pathCreatedObj = getMVC().perform(post(getPathTo(ADDRESSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"city\" : \"Toronto\" ,"
                        + "\"user\" : \"" + getPathToUser() + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that address is added
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("city", is("Toronto")));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_post_any_user_addresses() throws Exception {
        //users with ADMIN role can get from all addresses which are two
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdmin())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));

        //add address to other user
        String pathCreatedObj = getMVC().perform(post(getPathTo(ADDRESSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"city\" : \"Toronto\" ,"
                        + "\"user\" : \"" + getPathToUser() + "\"" +
                        "}")
                .header("Authorization", getAdmin())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that address is added
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdmin())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getAdmin())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("city", is("Toronto")));
    }

    @Test
    void when_user_is_anonymous_then_post_is_Unauthorized() throws Exception {
        //anonymous user cannot perform post
        getMVC().perform(post(getPathTo(ADDRESSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"city\" : \"Toronto\" " +
                        "}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_is_anonymous_then_patch_is_FORBIDDEN() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //anonymous users cannot patch to addresses path
        getMVC().perform(patch(getPathTo(ADDRESSES_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_patch_its_own_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with USER role cannot patch to other users addresses
        getMVC().perform(patch(getPathTo(ADDRESSES_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Milan\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //user with USER role can patch its own data
        getMVC().perform(patch(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Toronto\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check that city has successfully been updated
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Toronto"));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_patch_any_users_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with ADMIN role can patch to users path and change the city of another user to "Toronto"
        getMVC().perform(patch(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Toronto\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check the city has changed to "Toronto"
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$.city").value("Toronto"));
    }

    @Test
    void when_user_is_anonymous_then_put_is_FORBIDDEN() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //anonymous users cannot put to addresses path
        getMVC().perform(put(getPathTo(ADDRESSES_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_put_its_own_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with USER role cannot patch to other users addresses
        getMVC().perform(put(getPathTo(ADDRESSES_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Milan\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //user with USER role can patch its own address
        getMVC().perform(patch(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Toronto\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check that city has successfully been updated
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Toronto"));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_put_any_users_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with ADMIN role can patch to users path and change the city of another user to "Toronto"
        getMVC().perform(put(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"city\": \"Toronto\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check the city has changed to "Toronto"
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$.city").value("Toronto"));
    }

    @Test
    void when_user_is_anonymous_then_delete_is_FORBIDDEN() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //anonymous users cannot delete from addresses path
        getMVC().perform(delete(getPathTo(ADDRESSES_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_delete_its_own_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with USER role cannot patch to other users data
        getMVC().perform(delete(getPathTo(ADDRESSES_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //user with USER role can delete its own addresses
        getMVC().perform(delete(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check that there are one address and two users
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(getPathToList(USERS_PATH_NAME), hasSize(2)));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_delete_any_users_addresses() throws Exception {
        //check there is a user with city and state "Shiraz" with id =2
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.city").value("Shiraz"))
                .andExpect(jsonPath("$.state").value("Shiraz"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with ADMIN role can delete addresses from any users
        getMVC().perform(delete(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check that there are one address and two users
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(getPathToList(USERS_PATH_NAME), hasSize(2)));
    }

    @Test
    void when_user_is_removed_then_its_addresses_are_removed() throws Exception {
        //check that there are two addresses
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));

        //users with ADMIN role can delete from users path
        getMVC().perform(delete(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

        //check that there are one address and two users
        getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(getPathToList(USERS_PATH_NAME), hasSize(1)));
    }
}
