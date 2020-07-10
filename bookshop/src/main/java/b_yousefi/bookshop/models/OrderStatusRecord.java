package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by: b.yousefi
 * Date: 7/6/2020
 */
@Entity
@Data
@Table(name = "order_status")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class OrderStatusRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Every order status must belong to an order")
    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER__ORDER_STATUS"))
    private Order order;

    private OrderStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @UpdateTimestamp
    private Date updatedAt;

    @Lob
    @Size(max = 100000, message = "Message length cannot be more than 100000 characters")
    @Column(length = 100000)
    private String message;

    @PrePersist
    private void checkOrders() {
        this.getOrder().getUser().updateStatus(this.getOrder(), this);
    }
}
