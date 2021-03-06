package b_yousefi.bookshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Builder
@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(exclude = {"addresses", "orders"})
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_USER_username", columnNames = {"username"}),
        @UniqueConstraint(name = "UK_USER_email", columnNames = {"email"}),
        @UniqueConstraint(name = "UK_USER_phoneNumber", columnNames = {"phoneNumber"})
})
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "[a-zA-Z0-9]+([_ -]?[a-zA-Z0-9]){5,40}$", message = "Username doesn't follow the acceptable pattern")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @Column(updatable = false)
    @Builder.Default
    private String role = "ROLE_USER";

    @OneToOne(targetEntity = DBFile.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id", foreignKey = @ForeignKey(name = "FK_PICTURE__USER"))
    private DBFile picture;

    @Pattern(regexp = "^\\d{12}$", message = "Phone number is not acceptable")
    private String phoneNumber;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
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

    public boolean isAdmin() {
        return getRole().equals("ROLE_ADMIN");
    }

    @PrePersist
    private void createOpenOrder() {
        Order order = Order.builder()
                .user(this)
                .build();
        OrderStatusRecord orderStatusRecord = OrderStatusRecord.builder()
                .order(order)
                .status(OrderStatus.OPEN)
                .build();
        order.setOrderStatusRecords(List.of(orderStatusRecord));
        this.orders.add(order);
    }

    public void updateStatus(Order order, OrderStatusRecord toOrderStatusRecord) {
        OrderStatus toOrderStatus = toOrderStatusRecord.getStatus();
        if (toOrderStatus.equals(OrderStatus.OPEN)
                && !order.getCurrentStatus().getStatus().equals(OrderStatus.OPEN)) {
            Order openOrder = this.orders
                    .stream()
                    .filter(ord -> ord.getCurrentStatus().getStatus().equals(OrderStatus.OPEN))
                    .findFirst().orElse(null);
            assert openOrder != null;
            if (openOrder.getOrderItems().size() > 0) {
                openOrder.getOrderStatusRecords()
                        .add(
                                OrderStatusRecord.builder().status(OrderStatus.FUTURE).order(openOrder).build()
                        );
            } else {
                this.orders.remove(openOrder);
            }
        } else if (!toOrderStatus.equals(OrderStatus.OPEN)
                && order.getCurrentStatus().getStatus().equals(OrderStatus.OPEN)) {
            this.createOpenOrder();
        }
    }
}
