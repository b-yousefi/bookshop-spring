package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class, optional = false)
    private User user;

    @ManyToOne(targetEntity = Address.class, optional = false)
    private Address address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CreationTimestamp
    private Date placedAt;

    @UpdateTimestamp
    private Date updatedAt;
}
