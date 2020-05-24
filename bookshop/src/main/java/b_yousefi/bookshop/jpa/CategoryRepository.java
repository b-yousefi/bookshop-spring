package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @RestResource(rel = "byParentId", path = "byParentId")
    List<Category> findAllByParentCat_Id(@Param("parentId") Long id);
}
