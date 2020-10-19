package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Entity
@Data
@Table(name = "order_table")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(exclude = {"orderStatusRecords", "orderItems"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Every order must have a user")
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER__ORDER"))
    private User user;

    //    @NotNull(message = "Every order must have an address")
    @ManyToOne(targetEntity = Address.class)
    @JoinColumn(name = "address_id", foreignKey = @ForeignKey(name = "FK_ADDRESS__ORDER"))
    private Address address;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderStatusRecord> orderStatusRecords = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public OrderStatusRecord getCurrentStatus() {
        return orderStatusRecords
                .stream()
                .max(Comparator.comparing(OrderStatusRecord::getUpdatedAt))
                .orElse(null);
    }

    public List<OrderStatusRecord> getSortedOrderStatusRecords() {
        orderStatusRecords.sort(Comparator.comparing(OrderStatusRecord::getUpdatedAt, Comparator.reverseOrder()));
        return orderStatusRecords;
    }
}
