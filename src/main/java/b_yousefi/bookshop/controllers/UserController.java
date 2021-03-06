package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.entities.Order;
import b_yousefi.bookshop.entities.OrderStatus;
import b_yousefi.bookshop.entities.User;
import b_yousefi.bookshop.repositories.OrderRepository;
import b_yousefi.bookshop.repositories.UserRepository;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import b_yousefi.bookshop.web.assemblers.OrderDetailedModelAssembler;
import b_yousefi.bookshop.web.assemblers.OrderModelAssembler;
import b_yousefi.bookshop.web.assemblers.UserModelAssembler;
import b_yousefi.bookshop.web.models.OrderDetailedModel;
import b_yousefi.bookshop.web.models.OrderModel;
import b_yousefi.bookshop.web.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by: b.yousefi Date: 5/15/2020
 */
@RepositoryRestController
public class UserController {
    private final UserRepositoryUserDetailsService userDetailsService;
    private final UserModelAssembler userModelAssembler;
    private final OrderDetailedModelAssembler orderDetailedModelAssembler;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssemblerOrder;

    public UserController(UserRepositoryUserDetailsService userDetailsService, UserModelAssembler userModelAssembler,
            UserRepository userRepository, OrderRepository orderRepository,
            OrderDetailedModelAssembler orderDetailedModelAssembler, OrderModelAssembler orderModelAssembler,
            PagedResourcesAssembler<Order> pagedResourcesAssemblerOrder) {
        this.userDetailsService = userDetailsService;
        this.userModelAssembler = userModelAssembler;
        this.orderDetailedModelAssembler = orderDetailedModelAssembler;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderModelAssembler = orderModelAssembler;
        this.pagedResourcesAssemblerOrder = pagedResourcesAssemblerOrder;
    }

    @PatchMapping(value = "/users/{userId}")
    @ResponseBody
    public ResponseEntity<UserModel> saveUser(@PathVariable Long userId, @RequestBody User usr) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setPassword(usr.getPassword());
            user.setPicture(usr.getPicture());
            user.setEmail(usr.getEmail());
            user.setPhoneNumber(usr.getPhoneNumber());
            user.setFirstName(usr.getFirstName());
            user.setLastName(usr.getLastName());
            User savedUser = userDetailsService.save(user);
            UserModel userModel = userModelAssembler.toModel(savedUser);
            return new ResponseEntity<>(userModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/users/{userId}/shopping_cart")
    public ResponseEntity<OrderDetailedModel> getShoppingCart(@PathVariable Long userId,
            Authentication authentication) {
        if (!hasLoggedInUserAccessToRequestedUser(authentication.getName(), userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Order> orderList = orderRepository.findOrderWithStatusAndUserId(userId, OrderStatus.OPEN);
        if (orderList.size() == 1) {
            Order openOrder = orderList.get(0);
            return new ResponseEntity<>(orderDetailedModelAssembler.toModel(openOrder), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean hasLoggedInUserAccessToRequestedUser(String loggedInUsername, Long requestUserId) {
        User user = userDetailsService.loadUserByUsername(loggedInUsername);
        if (user.isAdmin()) {
            return true;
        }
        return Objects.equals(user.getId(), requestUserId);
    }

    private boolean hasLoggedInUserAccessToRequestedUser(String loggedInUsername, String requestUsername) {
        User user = userDetailsService.loadUserByUsername(loggedInUsername);
        if (user.isAdmin()) {
            return true;
        }
        return Objects.equals(user.getUsername(), requestUsername);
    }

    @GetMapping(value = "/users/{userId}/orders")
    public ResponseEntity<CollectionModel<OrderModel>> getOrders(@PathVariable Long userId, Pageable pageable,
            Authentication authentication) {
        if (!hasLoggedInUserAccessToRequestedUser(authentication.getName(), userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Page<Order> orderEntities = orderRepository.findAllByUser_Id(userId, pageable);
        PagedModel<OrderModel> collModel = pagedResourcesAssemblerOrder.toModel(orderEntities, orderModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping(value = "/users/search/findUser")
    public ResponseEntity<UserModel> getUserByUsername(@RequestParam String username, Authentication authentication) {
        if (!hasLoggedInUserAccessToRequestedUser(authentication.getName(), username)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return userRepository.findByUsername(username).map(userModelAssembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId).map(userModelAssembler::toModel).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
