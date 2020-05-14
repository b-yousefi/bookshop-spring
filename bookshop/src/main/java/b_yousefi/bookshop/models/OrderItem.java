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
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne(targetEntity = Book.class)
    private Book book;
    @Builder.Default
    private int quantity = 1;

    @NonNull
    @ManyToOne(targetEntity = Order.class)
    private Order order;

}
