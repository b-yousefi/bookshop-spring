package b_yousefi.bookshop.integrated_tests;

import b_yousefi.bookshop.models.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by: b.yousefi
 * Date: 7/9/2020
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
public class OrderStatusRecordTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + ORDER_STATUSES_PATH_NAME;
    private String pathToOrder;

    @BeforeEach
    private void setup() throws Exception {
        String resultGetOrder = getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andReturn().getResponse().getContentAsString();
        pathToOrder = getObjectMapper().readTree(resultGetOrder).path("_links").path("self").path("href").asText();
    }

    @Test
    void when_user_is_anonymous_then_get_is_FORBIDDEN() throws Exception {
        //anonymous users cannot perform get on orders path
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void when_user_with_role_USER_then_can_get_its_own_order_statuses() throws Exception {
        //users with USER role can get the filtered data, all the order statues that belong to them
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME))
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));
        //get the information for order status with id = 2, which belongs to user with role USER, with its own credential
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //user with role USER cannot perform get on order statues path to get other users order statues
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_get_any_user_order_statuses() throws Exception {
        //users with ADMIN role can get all the order statuses
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(4)));
        //get the information for order status with id = 2 which belongs to another user
        getMVC().perform(get(getPathTo(ORDER_STATUSES_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));
    }

    @Test
    void when_perform_patch_get_error() throws Exception {
        //anonymous user cannot perform patch
        getMVC().perform(patch(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        //loggedIn user cannot perform patch
        getMVC().perform(patch(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //admin cannot perform patch
        getMVC().perform(patch(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());
    }


    @Test
    void when_perform_put_get_error() throws Exception {
        //anonymous user cannot perform put
        getMVC().perform(put(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        //loggedIn user cannot perform put
        getMVC().perform(put(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //admin cannot perform put
        getMVC().perform(put(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_perform_delete_get_error() throws Exception {
        //anonymous user cannot perform delete
        getMVC().perform(delete(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnauthorized());
        //loggedIn user cannot perform delete
        getMVC().perform(delete(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //admin cannot perform delete
        getMVC().perform(delete(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_order_status_changes_from_open_to_something_else_another_order_with_status_open_is_created() throws Exception {
        //current status of order with id = 2 is OPEN
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.history", hasSize(1)))
                .andExpect(jsonPath("$.currentStatus.status").value(OrderStatus.OPEN.name()))
                .andExpect(jsonPath("$.currentStatus.id").value("2"))
        ;

        //post order status to current user order with id = 2
        getMVC().perform(post(getPathTo(ORDER_STATUSES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{"
                        + "\"order\" : \"" + pathToOrder + "\" ,"
                        + "\"status\" : \"" + OrderStatus.FUTURE + "\"" +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isCreated());

        //current status of order with id = 2 is FUTURE and history has size = 2 and its items are in descending order
        getMVC().perform(get(getPathTo(ORDERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.history", hasSize(2)))
                .andExpect(jsonPath("$.history[1].status").value(OrderStatus.OPEN.name()))
                .andExpect(jsonPath("$.currentStatus.status").value(OrderStatus.FUTURE.name()))
        ;
    }
}
