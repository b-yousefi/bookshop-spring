package b_yousefi.bookshop.integrated_tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Created by: b.yousefi
 * Date: 5/18/2020
 */
@Service
@ActiveProfiles("test")
public class DatabaseCleanupService {
    @Autowired
    private EntityManager entityManager;
    private Set<String> tableNames = Set.of("user", "author", "address", "order_table", "order_item",
            "book", "publication", "category", "book_author", "book_category", "dbfile");

    @Transactional
    public void truncate() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        tableNames.forEach(tableName ->
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate()
        );
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
