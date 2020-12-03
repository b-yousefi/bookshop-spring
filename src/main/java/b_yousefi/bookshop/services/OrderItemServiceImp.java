package b_yousefi.bookshop.services;

import b_yousefi.bookshop.entities.OrderItem;
import b_yousefi.bookshop.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImp implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImp(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem addOrderItem(OrderItem orderItem) {
        OrderItem orderItem_ = orderItemRepository.save(orderItem);
        orderItem.getBook().putOrder(orderItem_.getQuantity());
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(OrderItem orderItem) {
        int changedCount = -orderItem.getQuantity();
        orderItem.getBook().putOrder(changedCount);
        orderItemRepository.delete(orderItem);
    }

    public OrderItem updateOrderItem(OrderItem prevOrderItem, int updatedQuantity) {
        int changedCount = updatedQuantity - prevOrderItem.getQuantity();
        prevOrderItem.getBook().putOrder(changedCount);
        prevOrderItem.setQuantity(updatedQuantity);
        return orderItemRepository.save(prevOrderItem);
    }
}
