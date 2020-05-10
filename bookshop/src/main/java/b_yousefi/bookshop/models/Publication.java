package b_yousefi.bookshop.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Data
@Entity
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Publication firstName is required")
    private String name;

    @OneToMany(targetEntity = Book.class)
    private List<Book> books = new ArrayList<>();
}
