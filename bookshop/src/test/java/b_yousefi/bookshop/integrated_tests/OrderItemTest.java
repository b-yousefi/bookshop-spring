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
    private String add_book_to_shopping_cart = ORDER_ITEMS_PATH_NAME + "/add_book_to_shopping_cart";

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
    void when_add_book_to_shopping_cart_without_order_id_gets_error() throws Exception {
        //there are 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //cannot add order without order field
        getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"book\" : \"" + pathToBook1 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_add_book_to_shopping_cart_without_book_id_gets_error() throws Exception {
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //cannot add order item without book field
        getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
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
        getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook1 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_order_quantity_is_greater_than_book_quantity_get_error() throws Exception {
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //quantity of book is 1
        getMVC().perform(get(pathToBook2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("quantity").value(1));

        //add an order item to current user order with id = 1
        getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\" ,"
                        + "\"quantity\" :   2 " +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_user_is_anonymous_then_post_is_Unauthorized() throws Exception {
        //anonymous user cannot perform post
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_is_anonymous_then_patch_is_Unauthorized() throws Exception {
        //anonymous user cannot perform patch
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_is_anonymous_then_put_is_Unauthorized() throws Exception {
        //anonymous user cannot perform put
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void direct_put_is_forbidden_for_user_and_admin() throws Exception {
        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());

        getMVC().perform(put(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
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
    void direct_patch_is_forbidden_for_user_and_admin() throws Exception {
        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());

        getMVC().perform(patch(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
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
    void direct_post_is_forbidden_for_user_and_admin() throws Exception {
        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());

        getMVC().perform(post(getPathTo(ORDER_ITEMS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void direct_delete_is_forbidden_for_user_and_admin() throws Exception {
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 1)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());

        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_is_anonymous_then_delete_is_Unauthorized() throws Exception {
        //anonymous user cannot perform delete
        getMVC().perform(delete(getPathTo(ORDER_ITEMS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void user_can_add_order_item_to_its_own_shopping_cart_for_user() throws Exception {
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //quantity of book is 1
        getMVC().perform(get(pathToBook2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("quantity").value(1));

        //add an order item to current user order with id = 1
        String pathCreatedObj = getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
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

        //after putting the order quantity of book changes from 1 to 0
        getMVC().perform(get(pathToBook2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("quantity").value(0));

        //post order to another user gets error
        getMVC().perform(post(getPathTo(add_book_to_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrderAdmin + "\" ,"
                        + "\"book\" : \"" + pathToBook2 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void user_can_remove_order_item_from_its_own_shopping_cart_for_user() throws Exception {
        String remove_book_from_shopping_cart = ORDER_ITEMS_PATH_NAME + "/remove_book_from_shopping_cart";
        //there is 1 order item for this user
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        //quantity of book is 3
        getMVC().perform(get(pathToBook1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("quantity").value(3));

        //add an order item to current user order with id = 1
        getMVC().perform(post(getPathTo(remove_book_from_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"id\" : \"" + 2 + "\" ," +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 1 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isNoContent());

        //check that order item is removed
        getMVC().perform(get(getPathTo(ORDER_ITEMS_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(0)));

        //after putting the order quantity of book changes from 3 to 4
        getMVC().perform(get(pathToBook1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("quantity").value(4));

        //post order to another user gets error
        getMVC().perform(post(getPathTo(remove_book_from_shopping_cart))
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "\"id\" : \"" + 1 + "\" ," +
                        "\"order\" : \"" + pathToOrder + "\" ," +
                        "\"book\" : \"" + pathToBook1 + "\" ," +
                        "\"quantity\" : \"" + 0 + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }
}
