package b_yousefi.bookshop.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import java.util.concurrent.TimeUnit;

/**
 * Created by: b.yousefi
 * Date: 6/6/2020
 */
//@Configuration
public class GlobalRepositoryRestConfigurer implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.getCorsRegistry()
                .addMapping("/**")
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.OPTIONS.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())
//                .allowedHeaders("*")
//                .allowCredentials(false)
//                .maxAge(1800L)
                .allowedOrigins("http://localhost:3000");

    }
}
