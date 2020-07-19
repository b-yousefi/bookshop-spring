package b_yousefi.bookshop.models.representations.assemblers;

import b_yousefi.bookshop.controllers.OrderController;
import b_yousefi.bookshop.jpa.OrderItemRepository;
import b_yousefi.bookshop.models.OrderItem;
import b_yousefi.bookshop.models.representations.OrderItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@Component
public class OrderItemModelAssembler extends ModelAssembler<OrderItem, OrderItemModel> {

    @Autowired
    private BookModelAssembler bookModelAssembler;

    public OrderItemModelAssembler(RepositoryRestConfiguration config) {
        super(OrderItemRepository.class, OrderItemModel.class, config);
    }

    @Override
    public OrderItemModel toModel(OrderItem orderItem) {
        return OrderItemModel.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .quantity(orderItem.getQuantity())
                .book(bookModelAssembler.toModel(orderItem.getBook()))
                .build()
                .add(fixLinkSelf(
                        methodOn(OrderController.class)
                                .getOrderItemById(orderItem.getId()))
                        .withSelfRel());
    }
}
