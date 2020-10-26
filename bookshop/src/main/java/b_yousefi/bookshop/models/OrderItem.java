package b_yousefi.bookshop.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
        @UniqueConstraint(name = "UK_ORDER_ITEM__order_book", columnNames = {"order_id", "book_id"})
})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Every order item must have a book")
    @ManyToOne(targetEntity = Book.class)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "FK_BOOK__ORDER_ITEM"), updatable = false)
    private Book book;

    @Builder.Default
    private int quantity = 1;

    @NotNull(message = "Every order item must belong to an order")
    @ManyToOne(targetEntity = Order.class)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER__ORDER_ITEM"))
    private Order order;
}
