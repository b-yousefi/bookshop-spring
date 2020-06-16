package b_yousefi.bookshop.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by: b.yousefi
 * Date: 6/16/2020
 */
public class TokenAuthenticationException extends AuthenticationException {
    public TokenAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public TokenAuthenticationException(String msg) {
        super(msg);
    }
}
