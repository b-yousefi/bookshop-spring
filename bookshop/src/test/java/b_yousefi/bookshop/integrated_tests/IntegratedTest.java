package b_yousefi.bookshop.integrated_tests;

import b_yousefi.bookshop.BookshopApplication;
import b_yousefi.bookshop.models.User;
import b_yousefi.bookshop.security.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by: b.yousefi
 * Date: 5/18/2020
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookshopApplication.class)
@TestPropertySource(locations = "classpath:application.yml")
public abstract class IntegratedTest {

    protected static final String BOOKS_PATH_NAME = "books";
    protected static final String CATEGORIES_PATH_NAME = "categories";
    protected static final String AUTHORS_PATH_NAME = "authors";
    protected static final String PUBLICATIONS_PATH_NAME = "publications";
    protected static final String USERS_PATH_NAME = "users";
    protected static final String ADDRESSES_PATH_NAME = "addresses";
    protected static final String ORDERS_PATH_NAME = "orders";
    protected static final String ORDER_ITEMS_PATH_NAME = "order_items";

    protected static final String BOOK_PATH_NAME = "book";
    protected static final String CATEGORY_PATH_NAME = "category";
    protected static final String AUTHOR_PATH_NAME = "author";
    protected static final String PUBLICATION_PATH_NAME = "publication";
    protected static final String USER_PATH_NAME = "user";
    protected static final String ADDRESS_PATH_NAME = "address";
    protected static final String ORDER_PATH_NAME = "order";
    protected static final String ORDER_ITEM_PATH_NAME = "order_item";


    protected String pathToUser, pathToAdmin;

    @Value("${spring.data.rest.base-path}")
    private String basePath;
    @Autowired
    private DatabaseCleanupService databaseCleanupService;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private FilterChainProxy filterChain;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private User admin, user;
    private String adminToken, userToken;

    @BeforeEach
    public void setupUsers() throws Exception {
        admin = User.builder().username("admin").password("admin").role("ROLE_ADMIN").build();
        user = User.builder().username("user_test1").password("user_test1").role("ROLE_USER").build();
        adminToken = createToken(admin);
        userToken = createToken(user);
        this.mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(filterChain).build();
        pathToUser = createPathToUser();
        pathToAdmin = createPathToAdmin();
    }

    private String createPathToUser() throws Exception {
        //get the url for user with id = 2
        String result = getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 2)
                .header("Authorization", getUserToken())
                .with(user(getUser().getUsername()).password(getUser().getPassword()).roles("USER")))
                .andExpect(jsonPath(getHrefFromLinks("self"), endsWith("2")))
                .andReturn().getResponse().getContentAsString();
        return getObjectMapper().readTree(result).path("_links").path("self").path("href").asText();
    }

    private String createPathToAdmin() throws Exception {
        //get the url for user with id = 1
        String result = getMVC().perform(get(getPathTo(USERS_PATH_NAME) + 1)
                .header("Authorization", getAdminToken())
                .with(user(getAdmin().getUsername()).password(getAdmin().getPassword()).roles("ADMIN")))
                .andExpect(jsonPath(getHrefFromLinks("self"), endsWith("1")))
                .andReturn().getResponse().getContentAsString();
        return getObjectMapper().readTree(result).path("_links").path("self").path("href").asText();
    }

    private String createToken(User user) {
        return jwtTokenUtil.generateToken(user);
    }

    @AfterEach
    public void cleanup() {
        databaseCleanupService.truncate();
    }

    public String getBasePath() {
        return basePath;
    }

    public String getSearchPathTo(String path) {
        return getBasePath() + "/" + path + "/search/";
    }


    public User getAdmin() {
        return admin;
    }

    public User getUser() {
        return user;
    }

    public String getAdminToken() {
        return adminToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public MockMvc getMVC() {
        return mvc;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getPathTo(String path) {
        return getBasePath() + "/" + path + "/";
    }

    public String getHrefFromLinks(String link) {
        return "_links." + link + ".href";
    }

    public String getPathToList(String path) {
        return "$._embedded." + path;
    }

    public String getPathToUser() {
        return pathToUser;
    }

    public String getPathToAdmin() {
        return pathToAdmin;
    }
}
