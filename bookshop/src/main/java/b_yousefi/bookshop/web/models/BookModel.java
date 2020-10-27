package b_yousefi.bookshop.web.models;

import b_yousefi.bookshop.entities.DBFile;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 6/27/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "book")
@Relation(collectionRelation = "books")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookModel extends RepresentationModel<BookModel> {
    private Long id;
    private String name;
    private Date publishedDay;
    private List<Long> authorIds;
    private Long publicationId;
    private String ISBN;
    private String summary;
    private List<Long> categoryIds;
    private DBFile picture;
    private BigDecimal price;
    private int quantity;
}
