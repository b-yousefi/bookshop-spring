package b_yousefi.bookshop.error_handling;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by: b.yousefi
 * Date: 6/16/2020
 */
@AllArgsConstructor
@Data
public class ErrorCause {
    private String causedBy;
    private String error;
}
