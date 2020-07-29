package b_yousefi.bookshop.jpa;

import b_yousefi.bookshop.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
public interface BookRepository extends CrudRepository<Book, Long> {
    @RestResource(rel = "byAuthorId", path = "byAuthorId")
    Page<Book> findByAuthors_Id(@Param("authorId") Long authorId, Pageable pageable);

    @RestResource(rel = "byPublicationId", path = "byPublicationId")
    Page<Book> findAllByPublication_Id(@Param("publicationId") Long publicationId, Pageable pageable);

    @RestResource(rel = "byCategoryId", path = "byCategoryId")
    Page<Book> findByCategories_Id(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("select distinct book from Book book where (book.publication.id in :publicationIds or :publicationIds is null) ")
    @RestResource(rel = "filterByPublication", path = "filterByPublication", exported = false)
    Page<Book> findByPublication_Ids(@Param("publicationIds") List<Long> publicationIds, Pageable pageable);

    @Query("select distinct book from Book book join book.authors author where author.id in :authorIds or :authorIds is null")
    @RestResource(rel = "filterByAuthor", path = "filterByAuthor", exported = false)
    Page<Book> findByAuthor_Ids(
            @Param("authorIds") List<Long> authorIds, Pageable pageable);

    @Query("select distinct book from Book book join book.categories category where category.id in :categoryIds or :categoryIds is null")
    @RestResource(rel = "filterByCategory", path = "filterByCategory", exported = false)
    Page<Book> findByCategory_Ids(
            @Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    @Query(value = "SELECT distinct book.* " +
            " FROM  book" +
            " INNER JOIN book_author" +
            " ON book_author.book_id = book.id" +
            " INNER JOIN book_category" +
            " ON book_category.book_id = book.id" +
            " WHERE (COALESCE(:publicationIds, NULL) IS NULL OR publication_id in :publicationIds)" +
            " AND (COALESCE(:authorIds, NULL) IS NULL OR book_author.author_id in :authorIds)" +
            " AND(COALESCE(:categoryIds, NULL) IS NULL OR book_category.category_id in :categoryIds)" +
            " ORDER BY book.name",
            countQuery = "SELECT count(distinct (book.id))" +
                    " FROM  book" +
                    " INNER JOIN book_author" +
                    " ON book_author.book_id = book.id" +
                    " INNER JOIN book_category" +
                    " ON book_category.book_id = book.id" +
                    " WHERE (COALESCE(:publicationIds, NULL) IS NULL OR publication_id in :publicationIds)" +
                    " AND (COALESCE(:authorIds, NULL) IS NULL OR book_author.author_id in :authorIds)" +
                    " AND(COALESCE(:categoryIds, NULL) IS NULL OR book_category.category_id in :categoryIds)"
            ,nativeQuery = true)
    @RestResource(rel = "filter", path = "filter")
    Page<Book> filter(@Param("publicationIds") List<Long> publicationIds,
                      @Param("categoryIds") List<Long> categoryIds, @Param("authorIds") List<Long> authorIds, Pageable pageable);

}
