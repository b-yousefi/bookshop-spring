package b_yousefi.bookshop.integrated_tests;

import b_yousefi.bookshop.models.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by: b.yousefi
 * Date: 5/17/2020
 */
@Sql("create_user.sql")
public class UserTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + USERS_PATH_NAME;

    @Test
    void register() throws Exception {
        getMVC().perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"username\" : \"b_yousefi\"" +
                        ", \"password\" : \"b_yousefi\" " +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}"))
                .andExpect(status().isCreated())
                //check that an order with status "open" is created while registering
                .andExpect(jsonPath("$.openOrder.currentStatus.status").value(OrderStatus.OPEN.name()));
    }

    @Test
    void authenticate() throws Exception {
        getMVC().perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"username\" : \"b_yousefi\"" +
                        ", \"password\" : \"b_yousefi\" " +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}"))
                .andExpect(status().isCreated());

        getMVC().perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"username\" : \"b_yousefi\"" +
                        ", \"password\" : \"b_yousefi\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void authenticate_with_wrong_password_gets_Unauthorized() throws Exception {
        getMVC().perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"username\" : \"b_yousefi\"" +
                        ", \"password\" : \"b_yousefi2\" " +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}"))
                .andExpect(status().isCreated());

        getMVC().perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"username\" : \"b_yousefi\"" +
                        ", \"password\" : \"b_yousefi\" }"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_is_anonymous_then_get_is_FORBIDDEN() throws Exception {
        //anonymous users cannot get from users path
        getMVC().perform(get(getPathTo(USERS_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_get_its_own_data() throws Exception {
        //get the information for user with id = 2, with its own credential
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.username").value(getUser().getUsername()))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //findUser
        getMVC().perform(get(getSearchPathTo(USERS_PATH_NAME) + "findUser")
                .param("username", "user_test1")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.username").value(getUser().getUsername()))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //user with role USER cannot get other users information
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 1)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());

        //findUser
        getMVC().perform(get(getSearchPathTo(USERS_PATH_NAME) + "findUser")
                .param("username", "admin")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_user_with_role_ADMIN_then_can_get_any_users() throws Exception {
        //users with ADMIN role can get from users path
        getMVC().perform(get(getPathTo(USERS_PATH_NAME)).header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());
        //findByUsername
        getMVC().perform(get(getSearchPathTo(USERS_PATH_NAME) + "findUser")
                .param("username", "user_test1")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$.username").value(getUser().getUsername()))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));
    }


    @Test
    void post_from_users_path_is_only_for_users_with_role_ADMIN() throws Exception {
        //check there are two users
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
        //anonymous users cannot post to users path
        getMVC().perform(post(getPathTo(USERS_PATH_NAME)))
                .andExpect(status().isUnauthorized());

        //users with USER role cannot post to users path
        getMVC().perform(post(getPathTo(USERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"username1\"," +
                        "  \"password\": \"username1\"," +
                        "  \"phoneNumber\": \"989352228877\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //users with ADMIN role can post to users path
        getMVC().perform(post(getPathTo(USERS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"username1\"," +
                        "  \"password\": \"username1\"," +
                        "  \"phoneNumber\": \"989352228877\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated());
        //check there are three users
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(3)));
    }

    @Test
    void when_user_is_anonymous_then_patch_is_FORBIDDEN() throws Exception {
        //check there are two users the username of the second one is "user_test1" with id =2
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1].username").value("user_test1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1]._links.self.href", endsWith("2")));

        //anonymous users cannot patch to users path
        getMVC().perform(patch(getPathTo(USERS_PATH_NAME)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void when_user_with_role_USER_then_can_patch_its_own_data() throws Exception {
        //check there is a user with username "user_test1" with id =2 and phone number = 989352229977
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.username").value("user_test1"))
                .andExpect(jsonPath("$.phoneNumber").value("989352229977"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with USER role cannot patch to other users data
        getMVC().perform(patch(getPathTo(USERS_PATH_NAME) + 1)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"username1\"," +
                        "  \"password\": \"username1\"," +
                        "  \"phoneNumber\": \"+989352225566\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //user with USER role can patch its own data
        getMVC().perform(patch(getPathTo(USERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"user_test1\"," +
                        "  \"password\": \"user_test1\"," +
                        "  \"phoneNumber\": \"989352229988\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());

        //check that phone number has successfully been updated
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath("$.username").value("user_test1"))
                .andExpect(jsonPath("$.phoneNumber").value("989352229988"));
    }

    @Test
    void when_user_with_role_ADMIN_then_can_patch_any_users() throws Exception {
        //check there is a user with username "user_test1" with id =2
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$.username").value("user_test1"))
                .andExpect(jsonPath("$._links.self.href", endsWith("2")));

        //users with ADMIN role can patch to users path and change the username and password to "username1"
        getMVC().perform(patch(getPathTo(USERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"username1\"," +
                        "  \"password\": \"username1\"," +
                        "  \"phoneNumber\": \"989352228866\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isOk());

        //check the username has changed to "username1"
        getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath("$.username").value("username1"));
    }

    @Test
    void put_from_users_path_is_only_for_users_with_role_ADMIN() throws Exception {
        //check there are two users the username of the second one is "user_test1" with id =2
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1].username").value("user_test1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1]._links.self.href", endsWith("2")));

        //anonymous users cannot  put to users path
        getMVC().perform(put(getPathTo(USERS_PATH_NAME)))
                .andExpect(status().isUnauthorized());

        //users with USER role cannot put to users path
        getMVC().perform(put(getPathTo(USERS_PATH_NAME)).header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //users with ADMIN role can put to users path and change the username and password to "username1"
        getMVC().perform(put(getPathTo(USERS_PATH_NAME) + 2)
                .contentType(MediaType.APPLICATION_JSON).content("{" +
                        "  \"username\": \"username1\"," +
                        "  \"password\": \"username1\"," +
                        "  \"phoneNumber\": \"989352228877\"" +
                        ", \"firstName\" : \"Behnaz\" " +
                        ", \"lastName\" : \"Yousefi\" " +
                        ", \"email\" : \"b.yousefi2912@gmail.com\" " +
                        "}")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());

        //check the username has changed to "username1"
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1].username").value("username1"));
    }

    @Test
    void delete_from_users_path_is_only_for_users_with_role_ADMIN() throws Exception {
        //check there are two users the username of the second one is "user_test1" with id =2
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1].username").value("user_test1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1]._links.self.href", endsWith("2")));

        //anonymous users cannot delete from users path
        getMVC().perform(delete(getPathTo(USERS_PATH_NAME) + 2))
                .andExpect(status().isUnauthorized());

        //users with USER role cannot delete from users path
        getMVC().perform(delete(getPathTo(USERS_PATH_NAME) + 2).header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //users with ADMIN role can delete from users path
        getMVC().perform(delete(getPathTo(USERS_PATH_NAME) + 2).header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check there is one user
        getMVC().perform(get(getPathTo(USERS_PATH_NAME))
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }
}
