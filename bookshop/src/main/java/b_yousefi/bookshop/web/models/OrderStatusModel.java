package b_yousefi.bookshop.web.models;

import b_yousefi.bookshop.entities.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "order_status")
@Relation(collectionRelation = "order_statuses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderStatusModel extends RepresentationModel<OrderStatusModel> {
    private Long id;
    private Long orderId;
    private OrderStatus status;
    private Date updatedAt;
    private String message;
}
