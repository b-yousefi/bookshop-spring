package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.models.User;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<User> saveUser(@PathVariable Long userId, @RequestBody User user) {
        user.setId(userId);
        User usr = userDetailsService.save(user);
        return new ResponseEntity<>(usr, HttpStatus.OK);
    }

}
