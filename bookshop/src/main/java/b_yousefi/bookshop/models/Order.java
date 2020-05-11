package b_yousefi.bookshop.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Entity
@Data
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date placedAt;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @OneToMany(targetEntity = OrderItem.class)
    private List<OrderItem> orderItems;

    @ManyToOne(targetEntity = Address.class)
    private Address addressOrder;

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }
}
