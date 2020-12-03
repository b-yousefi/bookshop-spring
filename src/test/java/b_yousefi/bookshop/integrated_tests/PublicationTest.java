package b_yousefi.bookshop.integrated_tests;

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
 * Date: 5/18/2020
 */
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_publication.sql")
        }
)
public class PublicationTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + PUBLICATIONS_PATH_NAME;

    @Test
    public void findAll() throws Exception {
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME))).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void publicationsByName() throws Exception {
        //check there is a publication named "oxford"
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"));

        //search for a publication which its name starts with "oxford" and check the result
        getMVC().perform(get(getSearchPathTo(PUBLICATIONS_PATH_NAME) + "publicationsByName").param("name", "oxfo"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"));
    }

    @Test
    void when_post_publication_with_ROLE_ADMIN_then_one_is_added_to_List() throws Exception {
        //check that users with role ADMIN have the permission to add a publication
        String pathCreatedObj = getMVC().perform(post(getPathTo(PUBLICATIONS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj))
                .andExpect(jsonPath("$.name").value("Cambridge"));
    }

    @Test
    void when_post_publication_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check that users with role USER don't have the permission to add a publication
        getMVC().perform(post(getPathTo(PUBLICATIONS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_patch_publication_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a publication named "oxford" and id = 1
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the publication name to "Cambridge"
        getMVC().perform(patch(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the publication name has changed to "Cambridge"
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Cambridge"));
    }

    @Test
    void when_patch_publication_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));

        getMVC().perform(patch(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_put_publication_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a publication named "oxford" and id = 1
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the publication name to "Cambridge" with ROLE_ADMIN
        getMVC().perform(put(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the publication name has changed to "Cambridge"
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Cambridge"));
    }

    @Test
    void when_put_publication_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a publication named "oxford" and id = 1
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("oxford"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //changing the publication name to "Cambridge" with ROLE_USER throws FORBIDDEN
        getMVC().perform(put(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Cambridge\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_delete_publication_with_ROLE_ADMIN_is_successful() throws Exception {
        //check there is one publication with id = 1
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //delete the publication with ROLE_ADMIN
        getMVC().perform(delete(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the publication list is empty
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(0)));
    }

    @Test
    void when_delete_publication_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is one publication with id = 1
        getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //deleting the publication with ROLE_USER throws FORBIDDEN
        getMVC().perform(delete(getPathTo(PUBLICATIONS_PATH_NAME) + "1")
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

}
