package b_yousefi.bookshop.models.representations.assemblers;

import b_yousefi.bookshop.controllers.OrderController;
import b_yousefi.bookshop.jpa.OrderStatusRecordRepository;
import b_yousefi.bookshop.models.OrderStatusRecord;
import b_yousefi.bookshop.models.representations.OrderStatusModel;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Component
public class OrderStatusModelAssembler extends ModelAssembler<OrderStatusRecord, OrderStatusModel> {

    public OrderStatusModelAssembler(RepositoryRestConfiguration config) {
        super(OrderStatusRecordRepository.class, OrderStatusModel.class, config);
    }

    @Override
    public OrderStatusModel toModel(OrderStatusRecord orderStatus) {
        return OrderStatusModel.builder()
                .id(orderStatus.getId())
                .status(orderStatus.getStatus())
                .orderId(orderStatus.getOrder().getId())
                .message(orderStatus.getMessage())
                .updatedAt(orderStatus.getUpdatedAt())
                .build()
                .add(fixLinkSelf(
                        methodOn(OrderController.class)
                                .getOrderStatusById(orderStatus.getId()))
                        .withSelfRel());
    }
}
