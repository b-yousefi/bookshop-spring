package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findAllByParentCat(Category parent);
}
