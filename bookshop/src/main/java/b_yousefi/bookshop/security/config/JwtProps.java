package b_yousefi.bookshop.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by: b.yousefi
 * Date: 5/11/2020
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProps {
    String secret = "default";
}
