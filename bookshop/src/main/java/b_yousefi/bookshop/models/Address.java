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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @NotNull(message = "Address must be marked on map")
    private Double latitude;

    @NotNull(message = "Address must be marked on map")
    private Double longitude;

    private String state;

    private String city;

    private String zipCode;

    @NotNull(message = "Address must have a user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER__ADDRESS"))
    private User user;
}
