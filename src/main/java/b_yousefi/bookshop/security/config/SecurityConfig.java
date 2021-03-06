package b_yousefi.bookshop.security.config;

import b_yousefi.bookshop.services.UserRepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by: b.yousefi Date: 5/10/2020
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserRepositoryUserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/**/api-docs/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**/authenticate", "/**/register").permitAll()
                .antMatchers(HttpMethod.PATCH, "/**/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/**/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/**/order_statuses/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/**/order_statuses/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/**/order_items/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/**/order_items/update_shopping_cart").hasAnyRole("USER", "ADMIN")
                .antMatchers("/**/addresses/**", "/**/orders/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/**/books/**", "/**/authors/**", "/**/publications/**",
                        "/**/categories/**")
                .permitAll()
                .antMatchers("/**/users/**", "/**/dBFiles/**", "/**/books/**", "/**/authors/**", "/**/publications/**",
                        "/**/categories/**")
                .hasRole("ADMIN").antMatchers("/**").denyAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration
                .setAllowedOrigins(List.of("http://localhost:3000", "https://byousefi.ir", "https://www.byousefi.ir"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "OPTION", "DELETE"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(1800L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
