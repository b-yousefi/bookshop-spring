package b_yousefi.bookshop.models.validators.BeforeCreate;

import b_yousefi.bookshop.models.Address;
import b_yousefi.bookshop.models.OrderItem;
import b_yousefi.bookshop.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by: b.yousefi
 * Date: 5/16/2020
 */
@Component("beforeCreateOrderItemValidator")
public class OrderItemValidator implements Validator {
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
//        if (!user.isAdmin() && !orderItem.getOrder().getUser().getId().equals(user.getId())) {
//            errors.rejectValue("user", "user id doesn't belong to the Logged in User");
//        }
    }
}
