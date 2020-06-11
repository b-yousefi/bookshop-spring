package b_yousefi.bookshop.models.validators.BeforeSave;

import b_yousefi.bookshop.jpa.OrderItemRepository;
import b_yousefi.bookshop.models.OrderItem;
import b_yousefi.bookshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by: b.yousefi
 * Date: 5/16/2020
 */
@Component("beforeSaveOrderItemValidator")
public class OrderItemValidator implements Validator {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(OrderItem.class);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        //todo: remove class later
//        OrderItem orderItem = (OrderItem) obj;
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        Optional<OrderItem> orderItemOP = orderItemRepository.findById(orderItem.getId());
//        if (orderItemOP.isPresent()) {
//            if (!user.isAdmin() && (!orderItem.getOrder().getUser().getId().equals(user.getId())
//                    || !orderItemOP.get().getOrder().getUser().getId().equals(user.getId()))) {
//                errors.rejectValue("user", "user id doesn't belong to the Logged in User");
//            }
//        } else {
//            errors.rejectValue("orderItem", "orderItem doesn't exist");
//        }
    }
}
