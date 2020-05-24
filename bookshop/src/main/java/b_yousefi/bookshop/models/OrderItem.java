package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "book_id"})
})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Book.class, optional = false)
    private Book book;

    @Builder.Default
    private int quantity = 1;

    @ManyToOne(targetEntity = Order.class, optional = false)
    private Order order;

}
