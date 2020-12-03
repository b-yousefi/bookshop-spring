package b_yousefi.bookshop.integrated_tests;

import b_yousefi.bookshop.entities.Author;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
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
@SqlGroup(
        {
                @Sql("create_user.sql"),
                @Sql("create_author.sql")
        }
)
public class AuthorTest extends IntegratedTest {
    private static String JSON_PATH_TO_LIST = "$._embedded." + AUTHORS_PATH_NAME;
    @LocalServerPort
    int randomPort;
    @Autowired
    private TestRestTemplate restTemplate;

    public String getBasePathTrv() {
        return "http://localhost:" + randomPort + getBasePath() + "/";
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Test
    public void findAll() {
        //check there is one author
        ResponseEntity<PagedModel<Author>> responseEntity = getRestTemplate().exchange(
                getPathTo(AUTHORS_PATH_NAME), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        PagedModel<Author> resources = responseEntity.getBody();
        Collection<Author> authors = resources.getContent();
        List<Author> authorList = new ArrayList<>(authors);
        assertThat(authorList).hasSize(1);
    }

    @Test
    void authorsByName() {
        //get the author
        Author author = getAuthor();
        Map<String, String> params = new HashMap<>();
        //set search parameter equal to the second half of the author's full name
        params.put("name", author.getFullName().substring(author.getFullName().length() / 2));
        ResponseEntity<EntityModel<Author>> responseEntity = getRestTemplate().exchange(
                getSearchPathTo(AUTHORS_PATH_NAME) + "authorsByName", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, params);
        EntityModel<Author> resources = responseEntity.getBody();
        //check the result is not null
        Author authorResult = resources.getContent();
        assertThat(authorResult).isNotNull();
    }

    @Test
    public void givenDataIsJson_whenDataIsPostedByPerform_With_ROLE_ADMIN_thenObjectIsCreated() throws Exception {
        //check there is one author
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME))).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        String json = "{\"fullName\":\"Foroq Farokhzad\"}";
        //check that users with role ADMIN have the permission to add a author
        getMVC().perform(post(getPathTo(AUTHORS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content(json).header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated());
        //check that author has been added and therefore there are two authors
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME))).andDo(print()).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(2)));
    }

    @Test
    public void givenDataIsJson_whenDataIsPostedByPostForObject_With_ROLE_USER_then_GET_FORBIDDEN() throws Exception {
        //check there is one author
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME))).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));

        Author author = Author.builder().fullName("Foroq Farokhzad").build();
        String json = new ObjectMapper().writeValueAsString(author);
        //check that users with role USER don't have the permission to add a author
        getMVC().perform(post(getPathTo(AUTHORS_PATH_NAME)).header("Authorization", getUserToken())
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(status().isForbidden());
        //check there is still one author
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME))).andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)));
    }

    @Test
    void when_post_author_with_ROLE_ADMIN_then_one_is_added_to_List() throws Exception {
        //check that users with role ADMIN have the permission to add a publication
        String pathCreatedObj = getMVC().perform(post(getPathTo(AUTHORS_PATH_NAME))
                .contentType(MediaType.APPLICATION_JSON).content("{ \"fullName\" : \"Foroq Farokhzad\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getRedirectedUrl();

        assert pathCreatedObj != null;
        getMVC().perform(get(pathCreatedObj))
                .andExpect(jsonPath("$.fullName").value("Foroq Farokhzad"));
    }

    @Test
    void when_patch_author_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is an author named "Jane Austen" and id = 1
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].fullName").value("Jane Austen"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the author name to "Austen" with ROLE_ADMIN
        getMVC().perform(patch(getPathTo(AUTHORS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"fullName\" : \"Austen\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the publication name has changed to "Austen"
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].fullName").value("Austen"));
    }

    @Test
    public void givenDataIsObject_whenDataIsExchangedByPatch_With_ROLE_USER_Throws_Access_Exception() {
        //get the existing author
        Author author = getAuthor();
        //change the existing author description to "Something else by user"
        author.setDescription("Something else by user");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + getUserToken());
        HttpEntity<Author> request = new HttpEntity<>(author, headers);
        //send the updated object with patch method and ROLE_USER and receive FORBIDDEN
        ResponseEntity<String> result =
                getRestTemplate().exchange(getPathTo(AUTHORS_PATH_NAME) + "1", HttpMethod.PATCH, request, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        //get the existing author and check that the description has not been changed
        Author authorResult = getAuthor();
        assertThat(authorResult).isNotNull();
        assertThat(author.getDescription()).isNotEqualTo(authorResult.getDescription());
    }

    @Test
    void when_put_author_with_ROLE_ADMIN_then_updated() throws Exception {
        //check there is an author named "Jane Austen" and id = 1
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].fullName").value("Jane Austen"))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //change the author name to "Austen" with ROLE_ADMIN
        getMVC().perform(put(getPathTo(AUTHORS_PATH_NAME) + "1")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"fullName\" : \"Austen\" }").header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the publication name has changed to "Austen"
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0].fullName").value("Austen"));
    }

    @Test
    public void givenDataIsObject_whenDataIsExchangedByPut_With_ROLE_USER_GET_FORBIDDEN() {
        //get the existing author
        Author author = getAuthor();
        //change the existing author description to "Something else by user"
        author.setDescription("Something else by user");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + getUserToken());
        HttpEntity<Author> request = new HttpEntity<>(author, headers);
        //send the updated object with put method and ROLE_USER and receive FORBIDDEN
        ResponseEntity<String> result =
                getRestTemplate().exchange(getPathTo(AUTHORS_PATH_NAME) + "1", HttpMethod.PUT, request, String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        //get the existing author and check that the description has not been changed
        Author authorResult = getAuthor();
        assertThat(author.getDescription()).isNotEqualTo(authorResult.getDescription());
    }

    @Test
    public void givenDataIsObject_whenDelete_With_ROLE_USER_then_GET_FORBIDDEN() {
        //check there is an author
        Author author = getAuthor();
        assertThat(author).isNotNull();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + getUserToken());
        HttpEntity<?> request = new HttpEntity<Object>(headers);
        //send the delete method with ROLE_USER for the only author with id = 1 and receive FORBIDDEN
        ResponseEntity<String> result =
                getRestTemplate().exchange(getPathTo(AUTHORS_PATH_NAME) + "1", HttpMethod.DELETE, request, String.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        //get the existing authors list and check its size is equal to 1
        ResponseEntity<PagedModel<Author>> responseEntity = getRestTemplate().exchange(
                getPathTo(AUTHORS_PATH_NAME), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        PagedModel<Author> resources = responseEntity.getBody();
        Collection<Author> authors = resources.getContent();
        List<Author> authorList = new ArrayList<>(authors);
        assertThat(authorList).hasSize(1);
    }

    @Test
    void when_delete_author_with_ROLE_ADMIN_is_successful() throws Exception {
        //check there is one author with id = 1
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST + "[0]._links.self.href", endsWith("1")));
        //delete the author with ROLE_ADMIN
        getMVC().perform(delete(getPathTo(AUTHORS_PATH_NAME) + "1")
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(status().isNoContent());
        //check the author list is empty
        getMVC().perform(get(getPathTo(AUTHORS_PATH_NAME)))
                .andExpect(jsonPath(JSON_PATH_TO_LIST, hasSize(0)));
    }

    @Test
    public void getAuthorWithTraverson() throws URISyntaxException {
        ParameterizedTypeReference<EntityModel<Author>> resourceParameterizedTypeReference = new ParameterizedTypeReference<>() {
        };

        Traverson traverson = new Traverson(new URI(getBasePathTrv() + AUTHORS_PATH_NAME), MediaTypes.HAL_JSON);
        EntityModel<Author> authorResource = traverson.//
                follow(JSON_PATH_TO_LIST + "[0]._links.self.href").//
                toObject(resourceParameterizedTypeReference);
        assert authorResource != null;
        assertThat(authorResource.hasLink("self")).isTrue();
        assert authorResource.getLink("self").isPresent();
        assertThat(authorResource.getRequiredLink("self").expand().getHref())
                .isEqualTo(getBasePathTrv() + AUTHORS_PATH_NAME + "/1");
        Author author = authorResource.getContent();
        assert author != null;
        assertThat(author.getFullName()).isEqualTo("Jane Austen");

    }

    public Author getAuthor() {
        ResponseEntity<PagedModel<Author>> responseEntity = getRestTemplate().exchange(
                getPathTo(AUTHORS_PATH_NAME), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        PagedModel<Author> resources = responseEntity.getBody();

        Collection<Author> authors = resources.getContent();
        resources.getLink("self");
        List<Author> authorList = new ArrayList<>(authors);
        assertThat(authorList).hasSize(1);


        return authorList.get(0);
    }

}
