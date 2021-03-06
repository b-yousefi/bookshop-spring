package b_yousefi.bookshop.web.assemblers;

import b_yousefi.bookshop.controllers.UserController;
import b_yousefi.bookshop.entities.Order;
import b_yousefi.bookshop.entities.OrderStatus;
import b_yousefi.bookshop.entities.User;
import b_yousefi.bookshop.repositories.OrderRepository;
import b_yousefi.bookshop.repositories.UserRepository;
import b_yousefi.bookshop.web.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Created by: b.yousefi
 * Date: 7/9/2020
 */
@Component
public class UserModelAssembler extends ModelAssembler<User, UserModel> {
    @Autowired
    OrderDetailedModelAssembler orderDetailedModelAssembler;
    @Autowired
    OrderRepository orderRepository;

    public UserModelAssembler(RepositoryRestConfiguration config) {
        super(UserRepository.class, UserModel.class, config);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel userModel = instantiateModel(entity);
        userModel.setId(entity.getId());
        userModel.setUsername(entity.getUsername());
        userModel.setEmail(entity.getEmail());
        userModel.setFirstName(entity.getFirstName());
        userModel.setLastName(entity.getLastName());
        userModel.setPhoneNumber(entity.getPhoneNumber());
        userModel.setPicture(entity.getPicture());
        userModel.setAdmin(entity.isAdmin());
        List<Order> orders = orderRepository.findOrderWithStatusAndUserId(entity.getId(), OrderStatus.OPEN);
        assert orders.size() == 1;
        userModel.setOpenOrder(orderDetailedModelAssembler.toModel(orders.get(0)));
        userModel.add(fixLinkSelf(
                methodOn(UserController.class)
                        .getUserById(entity.getId()))
                .withSelfRel());
        return userModel;
    }
}
