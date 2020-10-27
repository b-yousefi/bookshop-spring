package b_yousefi.bookshop.repositories;

import b_yousefi.bookshop.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by: b.yousefi
 * Date: 5/18/2020
 */
@DataJpaTest
@ActiveProfiles("test")
public abstract class DataTest {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private DBFileRepository dbFileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    public TestEntityManager getEntityManager() {
        return entityManager;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public DBFileRepository getDbFileRepository() {
        return dbFileRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public AuthorRepository getAuthorRepository() {
        return authorRepository;
    }

    public PublicationRepository getPublicationRepository() {
        return publicationRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public OrderItemRepository getOrderItemRepository() {
        return orderItemRepository;
    }

    public DBFile createDBFile() {
        DBFile dbFile = null;
        try {
            File file = ResourceUtils.getFile("src/test/resources/Jane_Austen.jpg");
            dbFile = DBFile.builder()
                    .fileName(file.getName())
                    .fileType("image")
                    .data(Files.readAllBytes(file.toPath()))
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dbFile != null)
            entityManager.persistAndFlush(dbFile);
        return dbFile;
    }

    public User createUser() {
        User user = User.builder()
                .username("b_yousefi")
                .firstName("Behnaz")
                .lastName("Yousefi")
                .email("b.yousefi2911@gmail.com")
                .phoneNumber("989352229966")
                .password("b_yousefi")
                .build();
        entityManager.persistAndFlush(user);
        return user;
    }

    public Address createAddress() {
        User user = createUser();
        Address address = Address.builder()
                .state("Tehran")
                .city("Tehran")
                .latitude(35.672746)
                .longitude(51.336169)
                .zipCode("123445")
                .user(user)
                .build();
        entityManager.persistAndFlush(address);
        return address;
    }

    public Author createAuthor() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1775, Calendar.DECEMBER, 16);
        Author author = Author.builder()
                .fullName("Jane Austen")
                .birthday(calendar.getTime())
                .description("Jane Austen was an English novelist known primarily for her six major novels," +
                        " which interpret, critique and comment upon the British landed gentry at the end of the 18th century. " +
                        "Austen's plots often explore the dependence of women on marriage in the pursuit of favourable social standing and economic security")
                .build();
        entityManager.persistAndFlush(author);
        return author;
    }

    public Category createCategory() {
        Category category = Category.builder()
                .name("Non-Fiction")
                .description("Nonfiction or non-fiction is any document, or content that purports in good faith " +
                        "to represent truth and accuracy regarding information, events, or people. Nonfiction content " +
                        "may be presented either objectively or subjectively, and may sometimes take the form of a story")
                .build();
        entityManager.persistAndFlush(category);
        return category;
    }

    public Publication createPublication() {
        Publication publication = Publication.builder()
                .name("Oxford")
                .website("www.oxford.com")
                .description("Oxford University Press (OUP) is the largest university press in the world," +
                        " and the second oldest after Cambridge University Press")
                .build();
        entityManager.persistAndFlush(publication);
        return publication;
    }

    public Book createBook() {
        List<Author> authors = new ArrayList<>();
        authors.add(createAuthor());
        Set<Category> categories = new HashSet<>();
        categories.add(createCategory());
        Book book = Book.builder()
                .name("Pride And Prejudice")
                .ISBN("0192802380")
                .summary("Pride and Prejudice is a romantic novel of manners written by Jane Austen in 1813. " +
                        "The novel follows the character development of Elizabeth Bennet, " +
                        "the dynamic protagonist of the book who learns about the repercussions of hasty judgments " +
                        "and comes to appreciate the difference between superficial goodness and actual goodness. " +
                        "Its humour lies in its honest depiction of manners, education, marriage, and money during the Regency era in Great Britain")
                .authors(authors)
                .publication(createPublication())
                .categories(categories)
                .quantity(3)
                .price(new BigDecimal(125))
                .build();
        entityManager.persistAndFlush(book);
        return book;
    }

    public Order createOrder() {
        Address address = createAddress();
        Order order = Order.builder()
                .user(address.getUser())
                .address(address)
                .build();
        entityManager.persistAndFlush(order);
        return order;
    }

    public OrderItem createOrderItem() {
        OrderItem orderItem = OrderItem.builder()
                .order(createOrder())
                .book(createBook())
                .quantity(2)
                .build();
        entityManager.persistAndFlush(orderItem);
        return orderItem;
    }
}
