package b_yousefi.bookshop.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */

@Data
@Entity
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"firstName", "lastName"})
)
@RequiredArgsConstructor
//@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
public class Author {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "firstName is required")
    private String firstName;
    @NotBlank(message = "lastName is required")
    private String lastName;
    private Date birthday;
    private String description;
    @ManyToOne(targetEntity = DBFile.class)
    private  DBFile picture;
}
