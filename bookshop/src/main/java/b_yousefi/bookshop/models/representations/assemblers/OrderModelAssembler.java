package b_yousefi.bookshop.models.representations.assemblers;

import b_yousefi.bookshop.controllers.OrderController;
import b_yousefi.bookshop.jpa.BookRepository;
import b_yousefi.bookshop.jpa.OrderRepository;
import b_yousefi.bookshop.jpa.OrderStatusRecordRepository;
import b_yousefi.bookshop.models.Order;
import b_yousefi.bookshop.models.OrderStatusRecord;
import b_yousefi.bookshop.models.representations.OrderModel;
import b_yousefi.bookshop.models.representations.OrderStatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.CollectionModel;
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
public class OrderModelAssembler extends ModelAssembler<Order, OrderModel> {

    @Autowired
    OrderStatusRecordRepository orderStatusRecordRepository;

    @Autowired
    OrderRepository orderRepository;

    public OrderModelAssembler(RepositoryRestConfiguration config) {
        super(BookRepository.class, OrderModel.class, config);
    }

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel orderModel = instantiateModel(entity);

        orderModel.add(fixLinkSelf(
                methodOn(OrderController.class)
                        .getOrderById(entity.getId()))
                .withSelfRel());

        orderModel.setId(entity.getId());
        if (entity.getAddress() != null)
            orderModel.setAddressId(entity.getAddress().getId());
        orderModel.setCurrentStatus(
                toOrderStatusModel(entity.getCurrentStatus(), orderModel));
        orderModel.setUserId(entity.getUser().getId());
        orderModel.setTotalPrice(orderRepository.getTotalPrice(entity.getId()));
        return orderModel;
    }

    @Override
    public CollectionModel<OrderModel> toCollectionModel(Iterable<? extends Order> entities) {
        return super.toCollectionModel(entities);
    }

    protected OrderStatusModel toOrderStatusModel(OrderStatusRecord orderStatus, OrderModel orderModel) {
        if (orderStatus == null) {
            return null;
        }
        return OrderStatusModel.builder()
                .id(orderStatus.getId())
                .status(orderStatus.getStatus())
                .orderId(orderModel.getId())
                .message(orderStatus.getMessage())
                .updatedAt(orderStatus.getUpdatedAt())
                .build()
                .add(fixLinkSelf(
                        methodOn(OrderController.class)
                                .getOrderStatusById(orderStatus.getId()))
                        .withSelfRel());
    }

    protected List<OrderStatusModel> toOrderStatusModel(List<OrderStatusRecord> orderStatuses, OrderModel orderModel) {
        if (orderStatuses.isEmpty())
            return Collections.emptyList();

        return orderStatuses.stream()
                .map(orderStatus -> toOrderStatusModel(orderStatus, orderModel))
                .collect(Collectors.toList());
    }


}
