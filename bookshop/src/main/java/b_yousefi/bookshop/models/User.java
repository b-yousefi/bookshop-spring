package b_yousefi.bookshop.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Builder
@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "[a-zA-Z0-9]+([_ -]?[a-zA-Z0-9]){5,40}$")
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String fullName;

    @ManyToOne(targetEntity = DBFile.class, cascade = CascadeType.ALL)
    private DBFile picture;

    @Column(unique = true)
    @Pattern(regexp = "^\\+98\\d{10}$")
    private String phoneNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
