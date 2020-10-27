package b_yousefi.bookshop.web.assemblers;

import b_yousefi.bookshop.controllers.OrderItemController;
import b_yousefi.bookshop.entities.OrderItem;
import b_yousefi.bookshop.repositories.OrderItemRepository;
import b_yousefi.bookshop.web.models.OrderItemModel;
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
                        methodOn(OrderItemController.class)
                                .getOrderItemById(orderItem.getId()))
                        .withSelfRel());
    }
}
