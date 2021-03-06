package b_yousefi.bookshop.entities;

import lombok.*;

import javax.persistence.*;

/**
 * Created by: b.yousefi Date: 5/10/2020
 */

@Data
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DBFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;
}
