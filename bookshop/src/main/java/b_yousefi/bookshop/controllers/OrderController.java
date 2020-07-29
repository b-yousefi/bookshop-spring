package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.jpa.OrderItemRepository;
import b_yousefi.bookshop.jpa.OrderRepository;
import b_yousefi.bookshop.jpa.OrderStatusRecordRepository;
import b_yousefi.bookshop.jpa.UserRepository;
import b_yousefi.bookshop.models.*;
import b_yousefi.bookshop.models.representations.OrderDetailedModel;
import b_yousefi.bookshop.models.representations.OrderItemModel;
import b_yousefi.bookshop.models.representations.OrderModel;
import b_yousefi.bookshop.models.representations.OrderStatusModel;
import b_yousefi.bookshop.models.representations.assemblers.OrderDetailedModelAssembler;
import b_yousefi.bookshop.models.representations.assemblers.OrderItemModelAssembler;
import b_yousefi.bookshop.models.representations.assemblers.OrderModelAssembler;
import b_yousefi.bookshop.models.representations.assemblers.OrderStatusModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 7/7/2020
 */
@RepositoryRestController
public class OrderController {
    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderStatusRecordRepository orderStatusRepository;
    private OrderModelAssembler orderModelAssembler;
    private OrderDetailedModelAssembler orderDetailedModelAssembler;
    private OrderItemModelAssembler orderItemModelAssembler;
    private OrderStatusModelAssembler orderStatusModelAssembler;
    private PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @Autowired
    OrderController(
            UserRepository userRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            OrderStatusRecordRepository orderStatusRepository,
            OrderModelAssembler orderModelAssembler,
            OrderDetailedModelAssembler orderDetailedModelAssembler,
            OrderItemModelAssembler orderItemModelAssembler,
            OrderStatusModelAssembler orderStatusModelAssembler,
            PagedResourcesAssembler<Order> pagedResourcesAssembler) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderModelAssembler = orderModelAssembler;
        this.orderDetailedModelAssembler = orderDetailedModelAssembler;
        this.orderItemModelAssembler = orderItemModelAssembler;
        this.orderStatusModelAssembler = orderStatusModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<CollectionModel<OrderModel>> getOrders(
            Pageable pageable) {
        User user = getUser();
        Page<Order> orderEntities;
        if (user.isAdmin()) {
            List<Order> orderList = orderRepository.findAll();
            orderEntities = new PageImpl<>(
                    orderList,
                    pageable,
                    orderList.size());
        } else {
            orderEntities = orderRepository.findAllByUser(user, pageable);
        }

        PagedModel<OrderModel> collModel = pagedResourcesAssembler
                .toModel(orderEntities, orderModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderDetailedModel> getOrderById(@PathVariable("id") Long id) {
        return orderRepository.findById(id)
                .map(orderDetailedModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/order_items/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderItemModel> getOrderItemById(@PathVariable("id") Long id) {
        return orderItemRepository.findById(id)
                .map(orderItemModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/order_items/update_shopping_cart", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderItemModel> updateShoppingCart(@RequestBody EntityModel<OrderItem> orderItemModel)
            throws Exception {
        OrderItem orderItem = orderItemModel.getContent();

        if (orderItem != null) {
            if (orderItem.getOrder() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (orderItem.getOrder().getCurrentStatus().getStatus().equals(OrderStatus.OPEN)) {
                if (orderItem.getId() != null) {
                    Optional<OrderItem> orderItemPrev = orderItemRepository.findById(orderItem.getId());
                    if (orderItemPrev.isPresent()) {
                        int changedCount = orderItem.getQuantity() - orderItemPrev.get().getQuantity();
                        if (orderItem.getQuantity() == 0) {
                            orderItemRepository.delete(orderItem);
                            orderItem.getBook().putOrder(changedCount);
                            orderRepository.save(orderItem.getOrder());
                            return new ResponseEntity<>(orderItemModelAssembler.toModel(orderItem), HttpStatus.OK);
                        } else {

                            OrderItem orderItem_ = orderItemRepository.save(orderItem);
                            orderItem.getBook().putOrder(changedCount);
                            orderRepository.save(orderItem.getOrder());
                            return new ResponseEntity<>(orderItemModelAssembler.toModel(orderItem_), HttpStatus.OK);
                        }
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                } else {
                    OrderItem orderItem_ = orderItemRepository.save(orderItem);
                    orderItem.getBook().putOrder(orderItem_.getQuantity());
                    orderRepository.save(orderItem.getOrder());
                    String selfLink = orderItemModelAssembler.toModel(orderItem_).getLink("self").map(Link::getHref).orElse("");
                    return ResponseEntity.created(new URI(selfLink)).body(orderItemModelAssembler.toModel(orderItem_));
                }
            } else {
                throw new Exception("Order is not OPEN");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/orders/close_shopping_cart", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderStatusModel> closeShoppingCart(@RequestBody EntityModel<Order> orderModel)
            throws Exception {

        Optional<Order> opOrder = orderRepository.findById(Objects.requireNonNull(orderModel.getContent()).getId());

        if (opOrder.isPresent()) {
            Order order = orderRepository.findById(opOrder.get().getId()).get();
            if (orderModel.getContent().getAddress() == null) {
                throw  new DataIntegrityViolationException("Order must have an address");
            }
            if (order.getOrderItems().size() == 0) {
                throw  new DataIntegrityViolationException("Order cannot be empty!");
            }
            order.setAddress(orderModel.getContent().getAddress());
            OrderStatusRecord orderStatusRecord = OrderStatusRecord.builder().order(order).status(OrderStatus.ORDERED).build();
            OrderStatusRecord saved = orderStatusRepository.save(orderStatusRecord);
            return new ResponseEntity<>(orderStatusModelAssembler.toModel(saved), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/order_statuses/{id}", method = RequestMethod.GET)
    public ResponseEntity<OrderStatusModel> getOrderStatusById(@PathVariable("id") Long id) {
        return orderStatusRepository.findById(id)
                .map(orderStatusModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private User getUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}
