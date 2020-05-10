package b_yousefi.bookshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by: b.yousefi
 * Date: 5/10/2020
 */
@Component
@ConfigurationProperties(prefix="spring.data.rest")
@Data
public class RestBasePath {
   // String
}
