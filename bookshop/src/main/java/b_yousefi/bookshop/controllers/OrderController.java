package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.entities.Order;
import b_yousefi.bookshop.entities.OrderStatus;
import b_yousefi.bookshop.entities.OrderStatusRecord;
import b_yousefi.bookshop.entities.User;
import b_yousefi.bookshop.repositories.OrderRepository;
import b_yousefi.bookshop.repositories.OrderStatusRecordRepository;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import b_yousefi.bookshop.web.assemblers.OrderDetailedModelAssembler;
import b_yousefi.bookshop.web.assemblers.OrderModelAssembler;
import b_yousefi.bookshop.web.assemblers.OrderStatusModelAssembler;
import b_yousefi.bookshop.web.models.OrderDetailedModel;
import b_yousefi.bookshop.web.models.OrderModel;
import b_yousefi.bookshop.web.models.OrderStatusModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by: b.yousefi Date: 7/7/2020
 */
@RepositoryRestController
public class OrderController {
    private final UserRepositoryUserDetailsService userDetailsService;
    private final OrderRepository orderRepository;
    private final OrderStatusRecordRepository orderStatusRepository;
    private final OrderModelAssembler orderModelAssembler;
    private final OrderDetailedModelAssembler orderDetailedModelAssembler;
    private final OrderStatusModelAssembler orderStatusModelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @Autowired
    OrderController(UserRepositoryUserDetailsService userDetailsService, OrderRepository orderRepository,
            OrderStatusRecordRepository orderStatusRepository, OrderModelAssembler orderModelAssembler,
            OrderDetailedModelAssembler orderDetailedModelAssembler,
            OrderStatusModelAssembler orderStatusModelAssembler,
            PagedResourcesAssembler<Order> pagedResourcesAssembler) {
        this.userDetailsService = userDetailsService;
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderModelAssembler = orderModelAssembler;
        this.orderDetailedModelAssembler = orderDetailedModelAssembler;
        this.orderStatusModelAssembler = orderStatusModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(value = "/orders")
    public ResponseEntity<CollectionModel<OrderModel>> getOrders(Pageable pageable) {
        User user = getUser();
        Page<Order> orderEntities;
        if (user.isAdmin()) {
            List<Order> orderList = orderRepository.findAll();
            orderEntities = new PageImpl<>(orderList, pageable, orderList.size());
        } else {
            orderEntities = orderRepository.findAllByUser(user, pageable);
        }

        PagedModel<OrderModel> collModel = pagedResourcesAssembler.toModel(orderEntities, orderModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<OrderDetailedModel> getOrderById(@PathVariable("id") Long id) {
        return orderRepository.findById(id).map(orderDetailedModelAssembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/users/{userId}/orders/{orderId}/close")
    @ResponseBody
    public ResponseEntity<OrderStatusModel> closeShoppingCart(Authentication authentication, @PathVariable Long userId,
            @PathVariable Long orderId, @RequestBody EntityModel<Order> orderModel) {
        Order orderRequest = orderModel.getContent();
        if (orderRequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Order> opOrder = orderRepository.findById(orderId);
        if (opOrder.isPresent() && canUpdateOrder(opOrder.get())
                && hasLoggedInUserAccessToRequestedOrder(authentication.getName(), userId, opOrder.get())) {
            Order order = opOrder.get();
            if (orderRequest.getAddress() == null) {
                throw new DataIntegrityViolationException("Order must have an address");
            }
            if (order.getOrderItems().isEmpty()) {
                throw new DataIntegrityViolationException("Order cannot be empty!");
            }
            order.setAddress(orderRequest.getAddress());
            OrderStatusRecord orderStatusRecord = OrderStatusRecord.builder().order(order).status(OrderStatus.ORDERED)
                    .build();
            OrderStatusRecord saved = orderStatusRepository.save(orderStatusRecord);
            return new ResponseEntity<>(orderStatusModelAssembler.toModel(saved), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/order_statuses/{id}")
    public ResponseEntity<OrderStatusModel> getOrderStatusById(@PathVariable("id") Long id) {
        return orderStatusRepository.findById(id).map(orderStatusModelAssembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userDetailsService.loadUserByUsername(authentication.getName());
    }

    private boolean hasLoggedInUserAccessToRequestedOrder(String loggedInUsername, Long requestUserId, Order order) {
        User user = userDetailsService.loadUserByUsername(loggedInUsername);
        if (user.isAdmin()) {
            return true;
        }
        Long orderUserId = order.getUser().getId();
        return Objects.equals(user.getId(), requestUserId) && orderUserId.equals(requestUserId);
    }

    private boolean canUpdateOrder(Order order) {
        return order.getCurrentStatus().getStatus().equals(OrderStatus.OPEN);
    }
}
