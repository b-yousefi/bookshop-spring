package b_yousefi.bookshop.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "order_detailed")
@Relation(collectionRelation = "orders_detailed")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailedModel extends OrderModel {
    List<OrderItemModel> orderItems;
    List<OrderStatusModel> history;
}
