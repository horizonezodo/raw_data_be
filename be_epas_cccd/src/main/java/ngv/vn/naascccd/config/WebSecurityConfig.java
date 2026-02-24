package ngv.vn.naascccd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {


    private final JwtAuthConverter jwtAuthConverter;
    public WebSecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(
                            HttpMethod.OPTIONS
                    )
                    .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
                            "/swagger-resources/**", "/swagger-ui.html","/actuator/prometheus/**");

        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(STATELESS));

        return http.build();
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("http://10.10.0.37:8000");
//        configuration.addAllowedOrigin("http://10.10.0.37:4200");
//        configuration.addAllowedOrigin("http://localhost:4200");
//        configuration.addAllowedOrigin("http://localhost:4202");
//        configuration.addAllowedOrigin("http://10.10.0.37:4202");
//        configuration.addAllowedOrigin("http://10.10.0.37:4203");
//        configuration.addAllowedOrigin("http://localhost:4203");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//    @Bean
//    public CorsFilter corsFilter() {
//        return new CorsFilter(corsConfigurationSource());
//    }

}
