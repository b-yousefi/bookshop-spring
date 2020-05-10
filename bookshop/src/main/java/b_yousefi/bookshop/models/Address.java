package b_yousefi.bookshop.models;

import b_yousefi.bookshop.security.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;



/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */

@Data
@Entity
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street;
    private final String state;
    private String city;
    private String zipCode;

    @ManyToOne(targetEntity = User.class)
    private User user;
}
