package b_yousefi.bookshop.models.representations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "order_item")
@Relation(collectionRelation = "order_items")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemModel extends RepresentationModel<OrderItemModel> {
    private Long id;
    private Long bookId;
    private int quantity;
    private Long orderId;
}
