package b_yousefi.bookshop.web;

import java.io.Serializable;

/**
 * Created by: b.yousefi
 * Date: 5/11/2020
 */
public class JwtResponse  implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
    public String getToken() {
        return this.jwttoken;
    }
}
