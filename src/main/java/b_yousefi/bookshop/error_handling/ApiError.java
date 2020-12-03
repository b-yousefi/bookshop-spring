package b_yousefi.bookshop.error_handling;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

/**
 * Created by: b.yousefi
 * Date: 6/16/2020
 */
@Data
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<ErrorCause> errors;

    public ApiError(HttpStatus status, String message, List<ErrorCause> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, ErrorCause error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
