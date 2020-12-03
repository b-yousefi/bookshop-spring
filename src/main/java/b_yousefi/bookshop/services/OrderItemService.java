package b_yousefi.bookshop.services;

import b_yousefi.bookshop.entities.OrderItem;

public interface OrderItemService {
    OrderItem addOrderItem(OrderItem orderItem);

    void deleteOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(OrderItem prevOrderItem, int updatedQuantity);
}
