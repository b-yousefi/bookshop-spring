package b_yousefi.bookshop.integrated_tests;

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
 * Date: 5/19/2020
 */
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_category.sql")
        }
)
public class CategoryTest extends IntegratedTest {

    private static String JSON_PATH_TO_LIST = "$._embedded." + CATEGORIES_PATH_NAME;

    @Test
    void findAll() throws Exception {
        //check there are two categories
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME))).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
    }

    @Test
    void findAllByParentCat_Id() throws Exception {
        //check there is one category with parent_id = 1
        getMVC().perform(
                get(getSearchPathTo(CATEGORIES_PATH_NAME) + "byParentId").param("parentId", "1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void when_post_category_with_ROLE_ADMIN_then_one_is_added_to_List() throws Exception {
        //check that users with role ADMIN have the permission to add a category
        String pathCreatedObj = getMVC().perform(post(getPathTo(CATEGORIES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Mythology\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN"))
        )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn().getResponse().getRedirectedUrl();

        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj))
                .andExpect(jsonPath("$.name").value("Mythology"));
    }

    @Test
    void when_post_category_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check that users with role USER don't have the permission to add a category
        getMVC().perform(post(getPathTo(CATEGORIES_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Mythology\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_put_category_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a category named "Fiction" and id = 1
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Fiction"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the category name to "Historical" with ROLE_ADMIN
        getMVC().perform(put(getPathTo(CATEGORIES_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Historical\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the category name has changed to "Historical"
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Historical"));
    }

    @Test
    void when_put_category_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a category named "Fiction" and id = 1
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Fiction"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //changing the category name to "Historical" with ROLE_USER throws FORBIDDEN
        getMVC().perform(put(getPathTo(CATEGORIES_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Historical\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_patch_category_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a category named "Fiction" and id = 1
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Fiction"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the category name to "Historical" with ROLE_ADMIN
        getMVC().perform(patch(getPathTo(CATEGORIES_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Historical\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the category name has changed to "Historical"
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Historical"));
    }

    @Test
    void when_patch_category_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a category named "Fiction" and id = 1
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Fiction"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //changing the category name to "Historical" with ROLE_USER throws FORBIDDEN
        getMVC().perform(patch(getPathTo(CATEGORIES_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Historical\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_delete_category_with_no_child_and_ROLE_ADMIN_then_its_removed() throws Exception {
        //check there are two categories and the second category id is equal to 2
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1]._links.self.href", endsWith("2")));
        //delete the category with id = 2 and ROLE_ADMIN
        getMVC().perform(delete(getPathTo(CATEGORIES_PATH_NAME) + "2")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the category has been deleted
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
    }

    @Test
    void when_delete_category_with_childs_and_ROLE_ADMIN_then_its_removed_with_all_its_children() throws Exception {
        //check there are two categories
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //there is one category with parent_id = 1
        getMVC().perform(
                get(getSearchPathTo(CATEGORIES_PATH_NAME) + "byParentId").param("parentId", "1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
        //delete the category with id = 1 and ROLE_ADMIN
        getMVC().perform(delete(getPathTo(CATEGORIES_PATH_NAME) + "1")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check two categories has been deleted
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(0)));
    }

    @Test
    void when_delete_category_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there are two categories and the second category id is equal to 2
        getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[1]._links.self.href", endsWith("2")));
        //delete the category with id = 2 and ROLE_USER throws FORBIDDEN
        getMVC().perform(delete(getPathTo(CATEGORIES_PATH_NAME) + "2")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }


}
