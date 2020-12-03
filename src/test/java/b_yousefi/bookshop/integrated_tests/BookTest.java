package b_yousefi.bookshop.integrated_tests;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
 * Date: 5/19/2020
 */
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_category.sql"),
                @Sql("create_author.sql"),
                @Sql("create_publication.sql"),
                @Sql("create_book.sql")
        }
)
public class BookTest extends IntegratedTest {

    private static final String JSON_PATH_TO_LIST = "$._embedded." + BOOKS_PATH_NAME;
    private String authorLink, publicationLink, categoryLink;

    @BeforeEach
    void setLinks() throws Exception {
        String result;

        //check there is one author and its id = 1, get the link to that resource
        result = getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(getPathToList(AUTHORS_PATH_NAME), hasSize(1)))
                .andExpect(jsonPath(getPathToList(AUTHORS_PATH_NAME) + "[0]." + getHrefFromLinks("self"), endsWith("1")))
                .andReturn().getResponse().getContentAsString();
        authorLink = getObjectMapper().readTree(result).path("_embedded").path(AUTHORS_PATH_NAME)
                .iterator().next().path("_links").path("self").path("href").asText();

        //check there is one publication and its id = 1, get the link to that resource
        result = getMVC().perform(get(getPathTo(PUBLICATIONS_PATH_NAME)))
                .andExpect(jsonPath(getPathToList(PUBLICATIONS_PATH_NAME), hasSize(1)))
                .andExpect(jsonPath(getPathToList(PUBLICATIONS_PATH_NAME) + "[0]." + getHrefFromLinks("self"), endsWith("1")))
                .andReturn().getResponse().getContentAsString();
        publicationLink = getObjectMapper().readTree(result).path("_embedded").path(PUBLICATIONS_PATH_NAME)
                .iterator().next().path("_links").path("self").path("href").asText();

        //check there are two categories and the first one id = 1, get the link to that resource
        result = getMVC().perform(get(getPathTo(CATEGORIES_PATH_NAME)))
                .andExpect(jsonPath(getPathToList(CATEGORIES_PATH_NAME), hasSize(2)))
                .andExpect(jsonPath(getPathToList(CATEGORIES_PATH_NAME) + "[0]." + getHrefFromLinks("self"), endsWith("1")))
                .andReturn().getResponse().getContentAsString();
        categoryLink = getObjectMapper().readTree(result).path("_embedded").path(CATEGORIES_PATH_NAME)
                .iterator().next().path("_links").path("self").path("href").asText();
    }

    @Test
    void findAll() throws Exception {
        //check there is one book
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Disabled("It does not work with h2 database!")
    @Test
    void filter() throws Exception {
        //check there is one book
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME) + "/filter")
                .param("publicationIds", "1,2"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }


    @Test
    void getBook_check_links() throws Exception {
        //check there is one book and its id = 1
        String result = getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]." + getHrefFromLinks("self"), endsWith("1")))
                .andReturn().getResponse().getContentAsString();
        //find the path to the existing book
        JsonNode jsonNode = getObjectMapper().readTree(result);
        String pathToBook = jsonNode.path("_embedded").path(BOOKS_PATH_NAME)
                .iterator().next().path("_links").path("self").path("href").asText();

        //get book with id = 1
        //check its name is equal to "Pride And Prejudice"
        getMVC().perform(get(pathToBook))
                .andExpect(jsonPath("name", is("Pride And Prejudice")));
        getMVC().perform(get(pathToBook))
                //check it has its author ids
                .andExpect(jsonPath("authorIds", contains(1)))
                //check it has its publication id
                .andExpect(jsonPath("publicationId").value(1))
                //check it has its categories ids
                .andExpect(jsonPath("categoryIds", contains(2)));
    }

    @Test
    void byAuthorId() throws Exception {
        getMVC().perform(get(getSearchPathTo(BOOKS_PATH_NAME) + "byAuthorId")
                .param("authorId", "1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void byPublicationId() throws Exception {
        getMVC().perform(get(getSearchPathTo(BOOKS_PATH_NAME) + "byPublicationId")
                .param("publicationId", "1"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void byCategoryId() throws Exception {
        getMVC().perform(get(getSearchPathTo(BOOKS_PATH_NAME) + "byCategoryId")
                .param("categoryId", "2"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void when_post_Book_with_ROLE_USER_it_gets_FORBIDDEN() throws Exception {
        //check that users with role USER don't have the permission to add a book
        getMVC().perform(post(getPathTo(BOOKS_PATH_NAME)).header("Authorization", getAdminToken())
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{}"
                )
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_post_Book_with_ROLE_ADMIN_and_no_publication_then_it_gets_conflict() throws Exception {
        //check that users with role ADMIN cannot add a book with no publication
        getMVC().perform(post(getPathTo(BOOKS_PATH_NAME)).header("Authorization", getAdminToken())
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"name\" : \"The Little Prince\" }"
                )
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_post_Book_with_ROLE_ADMIN_then_one_is_added_to_List() throws Exception {
        //check that users with role ADMIN have the permission to add a book
        String pathCreatedObj = getMVC().perform(post(getPathTo(BOOKS_PATH_NAME)).header("Authorization", getAdminToken())
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{ \"name\" : \"The Little Prince\"" +
                                ", \"publication\" : \"" + publicationLink + "\"" +
                                ",\"authors\" : [\"" + authorLink + "\"]" +
                                ",\"categories\" : [\"" + categoryLink + "\"]" +
                                " }"
                )
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();
        assert pathCreatedObj != null;
        //check the returned link
        getMVC().perform(get(pathCreatedObj))
                //check the name is as the one posted
                .andExpect(jsonPath("$.name").value("The Little Prince"))
                //check it has its author ids
                .andExpect(jsonPath("authorIds", contains(1)))
                //check it has its publication id
                .andExpect(jsonPath("publicationId").value(1))
                //check it has its categories ids
                .andExpect(jsonPath("categoryIds", contains(1)));
    }

    @Test
    void when_delete_author_with_ROLE_ADMIN_it_gets_deleted_from_book() throws Exception {
        //check that book with id = 1 has one author with id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME) + "1/" + AUTHORS_PATH_NAME))
                .andExpect(jsonPath(getPathToList(AUTHORS_PATH_NAME), hasSize(1)))
                .andExpect(jsonPath(getPathToList(AUTHORS_PATH_NAME) + "[0]._links.self.href", endsWith("1")));
        //delete the author with id = 1
        getMVC().perform(delete(getPathTo(AUTHORS_PATH_NAME) + "1")
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN"))
                .header("Authorization", getAdminToken()))
                .andExpect(status().isNoContent());
        //check that book with id = 1 has no author
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME) + "1/" + AUTHORS_PATH_NAME))
                .andExpect(jsonPath(getPathToList(AUTHORS_PATH_NAME), hasSize(0)));
    }

    @Test
    void when_patch_Book_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the book name to "Sense and Sensibility"
        getMVC().perform(patch(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Sense and Sensibility\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the book name has changed to "Sense and Sensibility"
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Sense and Sensibility"));
    }

    @Test
    void when_patch_book_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //check that user with role USER cannot change the book name
        getMVC().perform(patch(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Sense and Sensibility\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }


    @Test
    void when_put_Book_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the book name to "Sense and Sensibility"
        getMVC().perform(put(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Sense and Sensibility\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the book name has changed to "Sense and Sensibility"
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Sense and Sensibility"));
    }

    @Test
    void when_put_Book_with_ROLE_ADMIN_neagtive_quantity_then_get_error() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the book name to "Sense and Sensibility"
        getMVC().perform(put(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content(
                        "{" +
                                " \"name\" : \"Sense and Sensibility\" ," +
                                " \"quantity\" : \"-1\"" +
                                " }"
                ).header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isConflict());
    }

    @Test
    void when_put_book_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //check that user with role USER cannot change the book name
        getMVC().perform(put(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Sense and Sensibility\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void when_delete_Book_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //delete the book with ROLE_ADMIN
        getMVC().perform(delete(getPathTo(BOOKS_PATH_NAME) + "1")
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the book list is empty
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST).doesNotExist());
    }

    @Test
    void when_delete_book_with_ROLE_USER_then_gets_FORBIDDEN() throws Exception {
        //check there is a book named "Pride And Prejudice" and id = 1
        getMVC().perform(get(getPathTo(BOOKS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].name").value("Pride And Prejudice"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //deleting the book with ROLE_USER throws FORBIDDEN
        getMVC().perform(put(getPathTo(BOOKS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"name\" : \"Sense and Sensibility\" }").header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
    }

}
