package b_yousefi.bookshop.controllers;

import b_yousefi.bookshop.entities.User;
import b_yousefi.bookshop.security.JwtTokenUtil;
import b_yousefi.bookshop.security.TokenAuthenticationException;
import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import b_yousefi.bookshop.web.JwtRequest;
import b_yousefi.bookshop.web.JwtResponse;
import b_yousefi.bookshop.web.assemblers.UserModelAssembler;
import b_yousefi.bookshop.web.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by: b.yousefi Date: 5/11/2020
 */
@RestController
@BasePathAwareController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepositoryUserDetailsService userDetailsService;
    @Autowired
    private UserModelAssembler userModelAssembler;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new TokenAuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new TokenAuthenticationException("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserModel> saveUser(@RequestBody User user) {
        User createdUser = userDetailsService.save(user);
        return new ResponseEntity<>(userModelAssembler.toModel(createdUser), HttpStatus.CREATED);
    }

}
