package b_yousefi.bookshop.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private String state;
    private String city;
    private String zipCode;

    @NonNull
    @ManyToOne(targetEntity = User.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
