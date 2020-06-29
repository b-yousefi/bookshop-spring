package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.models.User;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;


/**
 * Created by: b.yousefi
 * Date: 5/15/2020
 */
@RepositoryRestController
public class UserController {
    private UserRepositoryUserDetailsService userDetailsService;

    public UserController(UserRepositoryUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    @ResponseBody
    public EntityModel<?> saveUser(@PathVariable Long userId, @RequestBody User user
            , PersistentEntityResourceAssembler assembler) {
        user.setId(userId);
        User usr = userDetailsService.save(user);
        return assembler.toFullResource(usr);
    }

}
