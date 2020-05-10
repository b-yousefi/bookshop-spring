package b_yousefi.bookshop.models;

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
//@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@RequiredArgsConstructor
public class DBFile {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

}
