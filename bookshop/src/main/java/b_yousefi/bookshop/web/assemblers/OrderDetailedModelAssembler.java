package b_yousefi.bookshop.web.assemblers;

import b_yousefi.bookshop.controllers.OrderController;
import b_yousefi.bookshop.entities.Order;
import b_yousefi.bookshop.entities.OrderItem;
import b_yousefi.bookshop.web.models.OrderDetailedModel;
import b_yousefi.bookshop.web.models.OrderItemModel;
import b_yousefi.bookshop.web.models.OrderModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderItemModelAssembler orderItemModelAssembler;

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
                        .getOrderById(entity.getId()))
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
                .map(orderItemModelAssembler::toModel)
                .collect(Collectors.toList());
    }

}
