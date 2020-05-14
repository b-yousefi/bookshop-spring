package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne(targetEntity = User.class)
    private User user;

    @NonNull
    @ManyToOne(targetEntity = Address.class)
    private Address addressOrder;

    @CreationTimestamp
    private Date placedAt;

    @UpdateTimestamp
    private Date updatedAt;
}
