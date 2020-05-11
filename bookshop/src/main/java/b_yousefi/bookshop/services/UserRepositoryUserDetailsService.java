package b_yousefi.bookshop.services;

import b_yousefi.bookshop.data.UserRepository;
import b_yousefi.bookshop.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

    public User save(User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
