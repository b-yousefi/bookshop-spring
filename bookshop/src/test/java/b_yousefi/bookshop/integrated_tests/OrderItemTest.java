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
                @Sql("create_category.sql"),
                @Sql("create_author.sql"),
                @Sql("create_publication.sql"),
                @Sql("create_book.sql"),
                @Sql("create_book2.sql"),
                @Sql("create_order.sql"),
                @Sql("create_order_item.sql"),
        }
)
public class OrderItemTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + ORDER_ITEMS_PATH_NAME;
    private String pathToOrder, pathToBook2, pathToBook1, pathToOrderAdmin;

    @BeforeEach
    private void setPaths() throws Exception {
        String resultGetOrder = getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andReturn().getResponse().getContentAsString();
        pathToOrder = getObjectMapper().readTree(resultGetOrder).path("_links").path("self").path("href").asText();
        resultGetOrder = getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 1)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andReturn().getResponse().getContentAsString();
        pathToOrderAdmin = getObjectMapper().readTree(resultGetOrder).path("_links").path("self").path("href").asText();
        String resultGetBook = getMVC().perform(get(getPathTo(BOOKS_PATH_NAME) + 1))
                .andReturn().getResponse().getContentAsString();
        pathToBook1 = getObjectMapper().readTree(resultGetBook).path("_links").path("self").path("href").asText();
        resultGetBook = getMVC().perform(get(getPathTo(BOOKS_PATH_NAME) + 2))
                .andReturn().getResponse().getContentAsString();
        pathToBook2 = getObjectMapper().readTree(resultGetBook).path("_links").path("self").path("href").asText();
    }

    @Test
    void when_user_is_anonymous_then_get_is_FORBIDDEN() throws Exception {
        //anonymous users cannot perform get on orders path
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME)))
                .andExpect(status().isUnauthorized());

        getMVC().perform(get(getSearchPathTo(ORDER_ITEMS_PATH_NAME) + "byOrderId")
                .param("orderId", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_get_its_own_order_items() throws Exception {
        //users with USER role can get the filtered data, all the order items that belong to them
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        //get the information for order item with id = 2, which belongs to user with role USER, with its own credential
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //get order items that belong to the order with id = 2, the order must belong to the logged in user
        getMVC().perform(get(getSearchPathTo(ORDER_ITEMS_PATH_NAME) + "byOrderId")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER"))
                .param("orderId", "2"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //user with role USER cannot perform get on orders path to get other users orders
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_get_any_user_order_items() throws Exception {
        //users with ADMIN role can get all the order items
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
        //get the information for order item with id = 2
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //get order items that belong to the order with id = 2, the order must belong to the logged in user
        getMVC().perform(get(getSearchPathTo(ORDER_ITEMS_PATH_NAME) + "byOrderId")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN"))
                .param("orderId", "2"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void when_post_order_item_without_order_id_gets_error() throws Exception {
        //there are 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //cannot add order without order field
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void when_post_order_without_book_id_gets_error() throws Exception {
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //cannot add order without user field
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" " +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_post_the_book_that_already_exist_in_order_gets_error() throws Exception {
        //there is 1 order item for this user and book_id = 1
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + "2/" + BOOK_PATH_NAME + "/1")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk());

        //add an order item to current user order with id = 1
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook1 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_user_with_role_USER_then_can_post_its_own_order_item() throws Exception {
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //add an order item to current user order with id = 1
        String pathCreatedObj = getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that order item is added
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk());

        //post order to another user gets error
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrderAdmin + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_post_any_user_order_item() throws Exception {
        //there are 2 order items
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));

        //add an order item to any user
        String pathCreatedObj = getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        //check that order item is added
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));
        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void when_user_is_anonymous_then_post_is_Unauthorized() throws Exception {
        //anonymous user cannot perform post
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_patch_its_own_order_item() throws Exception {
        //check there an order item with id =2, and quantity = 1
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("1"));

        //patch order to current user quantity
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //patch order to another user gets FORBIDDEN
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_patch_any_user_order_item() throws Exception {
        //check there an order item with id =2, and quantity = 1
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("1"));

        //patch order to any user
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

    }

    @Test
    void when_user_is_anonymous_then_patch_is_Unauthorized() throws Exception {
        //anonymous user cannot perform patch
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_put_its_own_order_item() throws Exception {
        //check there an order item with id =2, and quantity = 1
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("1"));

        //put order item to current user, change the quantity
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check the data has changed
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("2"));

        //put order item to another user gets FORBIDDEN
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_put_any_user_order_item() throws Exception {
        //check there an order item with id =2, and quantity = 1
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("1"));

        //put order item to any user, change the quantity
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

        //check the data has changed
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value("2"));
    }

    @Test
    void when_user_is_anonymous_then_put_is_Unauthorized() throws Exception {
        //anonymous user cannot perform put
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_delete_its_own_order_item() throws Exception {
        //user has 1 order item
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //delete order item from current user
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());
        //delete order from item other users gets Forbidden
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());

        //check user has 0 order item
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(0)));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_delete_any_user_order_item() throws Exception {
        //there are 2 order items
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));

        //delete order item from user
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //there are 1 order item
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

    }

    @Test
    void when_user_is_anonymous_then_delete_is_Unauthorized() throws Exception {
        //anonymous user cannot perform delete
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }
}
