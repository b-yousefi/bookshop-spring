package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Every order must have a user")
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER__ORDER"))
    private User user;

    @NotNull(message = "Every order must have an address")
    @ManyToOne(targetEntity = Address.class)
    @JoinColumn(name = "address_id", foreignKey = @ForeignKey(name = "FK_ADDRESS__ORDER"))
    private Address address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private Date placedAt;

    @UpdateTimestamp
    private Date updatedAt;
}
