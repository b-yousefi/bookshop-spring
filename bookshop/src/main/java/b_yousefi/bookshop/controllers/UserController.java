package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.jpa.OrderRepository;
import b_yousefi.bookshop.jpa.UserRepository;
import b_yousefi.bookshop.models.Order;
import b_yousefi.bookshop.models.OrderStatus;
import b_yousefi.bookshop.models.User;
import b_yousefi.bookshop.models.representations.OrderDetailedModel;
import b_yousefi.bookshop.models.representations.UserModel;
import b_yousefi.bookshop.models.representations.assemblers.OrderDetailedModelAssembler;
import b_yousefi.bookshop.models.representations.assemblers.UserModelAssembler;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * Created by: b.yousefi
 * Date: 5/15/2020
 */
@RepositoryRestController
public class UserController {
    private UserRepositoryUserDetailsService userDetailsService;
    private UserModelAssembler userModelAssembler;
    private OrderDetailedModelAssembler orderDetailedModelAssembler;
    private UserRepository userRepository;
    private OrderRepository orderRepository;

    public UserController(UserRepositoryUserDetailsService userDetailsService,
                          UserModelAssembler userModelAssembler,
                          UserRepository userRepository,
                          OrderRepository orderRepository,
                          OrderDetailedModelAssembler orderDetailedModelAssembler) {
        this.userDetailsService = userDetailsService;
        this.userModelAssembler = userModelAssembler;
        this.orderDetailedModelAssembler = orderDetailedModelAssembler;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<EntityModel<?>> saveUser(@PathVariable Long userId, @RequestBody User usr
            , PersistentEntityResourceAssembler assembler) {
        Optional<User> opUser = userRepository.findById(userId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setPassword(usr.getPassword());
            user.setPicture(usr.getPicture());
            user.setEmail(usr.getEmail());
            user.setPhoneNumber(usr.getPhoneNumber());
            user.setFirstName(usr.getFirstName());
            user.setLastName(usr.getLastName());
            return new ResponseEntity<>(assembler.toFullResource(userDetailsService.save(opUser.get())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/users/get_shopping_cart", method = RequestMethod.GET)
    public ResponseEntity<OrderDetailedModel> getShoppingCart(@RequestParam String username) {
        List<Order> orderList = orderRepository.findOrderWithStatusAndUserName(username, OrderStatus.OPEN);
        if (orderList.size() == 1) {
            Order openOrder = orderList.get(0);
            return new ResponseEntity<>(orderDetailedModelAssembler.toModel(openOrder), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserModel> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(userModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
