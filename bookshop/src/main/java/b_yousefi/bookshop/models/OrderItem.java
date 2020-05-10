package b_yousefi.bookshop.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = Book.class)
    private Book book;
    private int quantity;

}
