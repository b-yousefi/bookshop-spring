package b_yousefi.bookshop.integrated_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by: b.yousefi
 * Date: 5/23/2020
 */
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_address.sql"),
                @Sql("create_order.sql"),
        }
)
public class OrderTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + ORDERS_PATH_NAME;
    private String pathToAddress;

    @BeforeEach
    public void setPathToAddress() throws Exception {
        String result = getMVC().perform(get(getPathTo(ADDRESSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath(getHrefFromLinks("self"), endsWith("2")))
                .andReturn().getResponse().getContentAsString();
        pathToAddress = getObjectMapper().readTree(result).path("_links").path("self").path("href").asText();
    }

    @Test
    void when_user_is_anonymous_then_get_is_FORBIDDEN() throws Exception {
        //anonymous users cannot perform get on orders path
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME)))
                .andExpect(status().isUnauthorized());

        getMVC().perform(get(getSearchPathTo(ORDERS_PATH_NAME) + "myOrders")
                .param("username", getUser().getUsername()))
                .andExpect(status().isUnauthorized());

        getMVC().perform(get(getSearchPathTo(ORDERS_PATH_NAME) + "myOrdersRegisteredAtDate")
                .param("username", getUser().getUsername())
                .param("orderDate", "2020-05-18"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_get_its_own_orders() throws Exception {
        //users with USER role can get the filtered data, all the orders that belong to them
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));
        //get the information for orders with id = 2, which belongs to user with role USER, with its own credential
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //get all its own orders
        getMVC().perform(get(getSearchPathTo(ORDERS_PATH_NAME) + "myOrders")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER"))
                .param("username", getUser().getUsername()))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));

        //user with role USER cannot perform get on orders path to get other users orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_get_any_user_orders() throws Exception {
        //users with ADMIN role can get all the orders from orders path
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(4)));


        //get all its own orders
        getMVC().perform(get(getSearchPathTo(ORDERS_PATH_NAME) + "myOrders")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN"))
                .param("username", getAdmin().getUsername()))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void when_post_order_without_user_gets_error() throws Exception {
        //there are 3 orders for this user
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));

        //cannot add order without user field
        getMVC().perform(post(getPathTo(ORDERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void when_user_with_role_USER_then_can_post_its_own_orders() throws Exception {
        //there are 3 orders for this user
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));

        //add order to current user
        String pathCreatedObj = getMVC().perform(post(getPathTo(ORDERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"user\" : \"" + getPathToUser() + "\" ,"
                        + "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that order is added
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(4)));
        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk());

        //post order to another user gets error
        getMVC().perform(post(getPathTo(ORDERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"user\" : \"" + getPathToAdmin() + "\" ,"
                        + "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_post_any_user_orders() throws Exception {
        //there are 4 orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(4)));

        //add order to any user
        String pathCreatedObj = getMVC().perform(post(getPathTo(ORDERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"user\" : \"" + getPathToUser() + "\" ,"
                        + "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that order is added
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(5)));
        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void when_user_is_anonymous_then_post_is_Unauthorized() throws Exception {
        //anonymous user cannot perform post
        getMVC().perform(post(getPathTo(ORDERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_patch_its_own_orders() throws Exception {
        //check there is an order with id =2
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk());

        //patch order to current user
        getMVC().perform(patch(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToUser() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //patch order to another user gets FORBIDDEN
        getMVC().perform(patch(getPathTo(ORDERS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToAdmin() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_patch_any_user_orders() throws Exception {
        //check there is an order with id =2
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());

        //patch order to any user
        getMVC().perform(patch(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToUser() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

    }

    @Test
    void when_user_is_anonymous_then_patch_is_Unauthorized() throws Exception {
        //anonymous user cannot perform patch
        getMVC().perform(patch(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_put_its_own_orders() throws Exception {
        //check there is an order with id =2
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk());

        //put order to current user
        getMVC().perform(put(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToUser() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //put order to another user gets Forbidden
        getMVC().perform(put(getPathTo(ORDERS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToAdmin() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_put_any_user_orders() throws Exception {
        //check there is an order with id =2
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());

        //put order to any user
        getMVC().perform(put(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"user\" : \"" + getPathToUser() + "\" ," +
                        "\"address\" : \"" + pathToAddress + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

    }

    @Test
    void when_user_is_anonymous_then_put_is_Unauthorized() throws Exception {
        //anonymous user cannot perform put
        getMVC().perform(put(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_delete_its_own_orders() throws Exception {
        //user has 3 orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));

        //delete order from current user
        getMVC().perform(delete(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());
        //delete order from other users gets Forbidden
        getMVC().perform(delete(getPathTo(ORDERS_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());

        //check user has 2 orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_delete_any_user_orders() throws Exception {
        //there are 4 orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(4)));

        //delete order from user
        getMVC().perform(delete(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //there are 3 orders
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));

    }

    @Test
    void when_user_is_anonymous_then_delete_is_Unauthorized() throws Exception {
        //anonymous user cannot perform delete
        getMVC().perform(delete(getPathTo(ORDERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

}
