package b_yousefi.bookshop.models.representations.assemblers;

import b_yousefi.bookshop.controllers.OrderController;
import b_yousefi.bookshop.models.Order;
import b_yousefi.bookshop.models.OrderItem;
import b_yousefi.bookshop.models.representations.OrderDetailedModel;
import b_yousefi.bookshop.models.representations.OrderItemModel;
import b_yousefi.bookshop.models.representations.OrderModel;
import org.springframework.beans.BeanUtils;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Component
public class OrderDetailedModelAssembler extends OrderModelAssembler {

    public OrderDetailedModelAssembler(RepositoryRestConfiguration config) {
        super(config);
    }

    @Override
    public OrderDetailedModel toModel(Order entity) {
        OrderModel orderModel = super.toModel(entity);
        OrderDetailedModel orderDetailedModel = new OrderDetailedModel();
        BeanUtils.copyProperties(orderModel, orderDetailedModel);

        orderDetailedModel.add(fixLinkSelf(
                methodOn(OrderController.class)
                        .getOrderItemById(entity.getId()))
                .withSelfRel());

        orderDetailedModel.setOrderItems(
                toOrderItemModel(entity.getOrderItems(), orderModel));
        orderDetailedModel.setHistory(
                toOrderStatusModel(entity.getSortedOrderStatusRecords(), orderModel));
        return orderDetailedModel;
    }

    private List<OrderItemModel> toOrderItemModel(List<OrderItem> orderItems, OrderModel orderModel) {
        if (orderItems.isEmpty())
            return Collections.emptyList();

        return orderItems.stream()
                .map(orderItem -> OrderItemModel.builder()
                        .id(orderItem.getId())
                        .orderId(orderModel.getId())
                        .quantity(orderItem.getQuantity())
                        .bookId(orderItem.getBook().getId())
                        .build()
                        .add(fixLinkSelf(
                                methodOn(OrderController.class)
                                        .getOrderItemById(orderItem.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

}
