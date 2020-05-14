package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface BookRepository extends CrudRepository<Book, Long> {
    Page<Book> findByAuthors_Id(Long authorId, Pageable pageable);

    Page<Book> findAllByPublication_Id(Long publicationId, Pageable pageable);

    Page<Book> findByCategories_Id(Long categoryId, Pageable pageable);
}
