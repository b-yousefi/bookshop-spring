package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.entities.Order;
import b_yousefi.bookshop.entities.OrderItem;
import b_yousefi.bookshop.entities.OrderStatus;
import b_yousefi.bookshop.entities.User;
import b_yousefi.bookshop.repositories.OrderItemRepository;
import b_yousefi.bookshop.repositories.OrderRepository;
import b_yousefi.bookshop.services.OrderItemService;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import b_yousefi.bookshop.web.assemblers.OrderItemModelAssembler;
import b_yousefi.bookshop.web.models.OrderItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

@RepositoryRestController
public class OrderItemController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemModelAssembler orderItemModelAssembler;
    private final OrderItemService orderItemService;
    private final UserRepositoryUserDetailsService userDetailsService;

    @Autowired
    OrderItemController(OrderItemService orderItemService, UserRepositoryUserDetailsService userDetailsService,
            OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            OrderItemModelAssembler orderItemModelAssembler) {
        this.orderItemService = orderItemService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderItemModelAssembler = orderItemModelAssembler;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/order_items/{id}")
    public ResponseEntity<OrderItemModel> getOrderItemById(@PathVariable("id") Long id) {
        return orderItemRepository.findById(id).map(orderItemModelAssembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/users/{userId}/orders/{orderId}/order_items")
    @ResponseBody
    public ResponseEntity<OrderItemModel> addOrderItemToShoppingCart(Authentication authentication,
            @PathVariable Long userId, @PathVariable Long orderId, @RequestBody EntityModel<OrderItem> orderItemRequest)
            throws URISyntaxException {
        OrderItem orderItem = orderItemRequest.getContent();
        if (orderItem == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Order> opOrder = orderRepository.findById(orderId);
        if (opOrder.isPresent()
                && hasLoggedInUserAccessToRequestedOrder(authentication.getName(), userId, opOrder.get())
                && canUpdateOrder(opOrder.get())) {
            orderItem.setOrder(opOrder.get());
            OrderItem savedOrderItem = orderItemService.addOrderItem(orderItem);
            String selfLink = orderItemModelAssembler.toModel(savedOrderItem).getLink("self").map(Link::getHref)
                    .orElse("");
            return ResponseEntity.created(new URI(selfLink)).body(orderItemModelAssembler.toModel(savedOrderItem));
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/users/{userId}/orders/{orderId}/order_items/{orderItemId}")
    @ResponseBody
    public ResponseEntity<OrderItemModel> updateOrderItemInShoppingCart(Authentication authentication,
            @PathVariable Long userId, @PathVariable Long orderId, @PathVariable Long orderItemId,
            @RequestBody EntityModel<OrderItem> orderItemRequest) {
        OrderItem orderItem = orderItemRequest.getContent();
        if (orderItem == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<OrderItem> opOrderItem = orderItemRepository.findById(orderItemId);
        if (opOrderItem.isPresent()
                && hasLoggedInUserAccessToRequestedOrder(authentication.getName(), userId, opOrderItem.get().getOrder())
                && checkOrderContainsOrderItem(opOrderItem.get(), orderId)
                && canUpdateOrder(opOrderItem.get().getOrder())
                && orderItem.getBook().getId().equals(opOrderItem.get().getBook().getId())) {
            OrderItem savedOrderItem = orderItemService.updateOrderItem(opOrderItem.get(), orderItem.getQuantity());
            return new ResponseEntity<>(orderItemModelAssembler.toModel(savedOrderItem), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/users/{userId}/orders/{orderId}/order_items/{orderItemId}")
    @ResponseBody
    public ResponseEntity<OrderItemModel> deleteOrderItemFromShoppingCart(Authentication authentication,
            @PathVariable Long userId, @PathVariable Long orderId, @PathVariable Long orderItemId) {
        Optional<OrderItem> opOrderItem = orderItemRepository.findById(orderItemId);
        if (opOrderItem.isPresent()
                && hasLoggedInUserAccessToRequestedOrder(authentication.getName(), userId, opOrderItem.get().getOrder())
                && checkOrderContainsOrderItem(opOrderItem.get(), orderId)
                && canUpdateOrder(opOrderItem.get().getOrder())) {
            orderItemService.deleteOrderItem(opOrderItem.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkOrderContainsOrderItem(OrderItem orderItem, Long orderId) {
        return orderItem.getOrder().getId().equals(orderId);
    }

    private boolean canUpdateOrder(Order order) {
        return order.getCurrentStatus().getStatus().equals(OrderStatus.OPEN);
    }

    private boolean hasLoggedInUserAccessToRequestedOrder(String loggedInUsername, Long requestUserId, Order order) {
        User user = userDetailsService.loadUserByUsername(loggedInUsername);
        if (user.isAdmin()) {
            return true;
        }
        Long orderUserId = order.getUser().getId();
        return Objects.equals(user.getId(), requestUserId) && orderUserId.equals(requestUserId);
    }
}
