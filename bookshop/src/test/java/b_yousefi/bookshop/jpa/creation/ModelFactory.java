package b_yousefi.bookshop.jpa.creation;

import b_yousefi.bookshop.models.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: b.yousefi
 * Date: 5/14/2020
 */
public class ModelFactory {

    public static DBFile createDBFile(TestEntityManager entityManager) {
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

    public static User createUser(TestEntityManager entityManager) {
        User user = User.builder()
                .username("b_yousefi")
                .fullName("Behnaz Yousefi")
                .phoneNumber("+989352229966")
                .password("b_yousefi")
                .build();
        entityManager.persistAndFlush(user);
        return user;
    }

    public static Address createAddress(TestEntityManager entityManager) {
        User user = createUser(entityManager);
        Address address = Address.builder()
                .state("Tehran")
                .city("Tehran")
                .zipCode("123445")
                .user(user)
                .build();
        entityManager.persistAndFlush(address);
        return address;
    }

    public static Author createAuthor(TestEntityManager entityManager) {
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

    public static Category createCategory(TestEntityManager entityManager) {
        Category category = Category.builder()
                .name("Non-Fiction")
                .description("Nonfiction or non-fiction is any document, or content that purports in good faith " +
                        "to represent truth and accuracy regarding information, events, or people. Nonfiction content " +
                        "may be presented either objectively or subjectively, and may sometimes take the form of a story")
                .build();
        entityManager.persistAndFlush(category);
        return category;
    }

    public static Publication createPublication(TestEntityManager entityManager) {
        Publication publication = Publication.builder()
                .name("Oxford")
                .website("www.oxford.com")
                .description("Oxford University Press (OUP) is the largest university press in the world," +
                        " and the second oldest after Cambridge University Press")
                .build();
        entityManager.persistAndFlush(publication);
        return publication;
    }

    public static Book createBook(TestEntityManager entityManager) {
        Set<Author> authors = new HashSet<>();
        authors.add(createAuthor(entityManager));
        Set<Category> categories = new HashSet<>();
        categories.add(createCategory(entityManager));
        Book book = Book.builder()
                .name("Pride And Prejudice")
                .ISBN("0192802380")
                .summary("Pride and Prejudice is a romantic novel of manners written by Jane Austen in 1813. " +
                        "The novel follows the character development of Elizabeth Bennet, " +
                        "the dynamic protagonist of the book who learns about the repercussions of hasty judgments " +
                        "and comes to appreciate the difference between superficial goodness and actual goodness. " +
                        "Its humour lies in its honest depiction of manners, education, marriage, and money during the Regency era in Great Britain")
                .authors(authors)
                .publication(createPublication(entityManager))
                .categories(categories)
                .build();
        entityManager.persistAndFlush(book);
        return book;
    }

    public static Order createOrder(TestEntityManager entityManager) {
        Address address = createAddress(entityManager);
        Order order = Order.builder()
                .user(address.getUser())
                .addressOrder(address)
                .build();
        entityManager.persistAndFlush(order);
        return order;
    }

    public static OrderItem createOrderItem(TestEntityManager entityManager) {
        OrderItem orderItem = OrderItem.builder()
                .order(createOrder(entityManager))
                .book(createBook(entityManager))
                .build();
        entityManager.persistAndFlush(orderItem);
        return orderItem;
    }

}
