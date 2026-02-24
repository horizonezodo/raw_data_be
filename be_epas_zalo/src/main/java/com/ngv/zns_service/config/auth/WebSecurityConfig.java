// package com.ngv.zns_service.config.auth;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;

// import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class WebSecurityConfig {


//     private final JwtAuthConverter jwtAuthConverter;
// //    private final JwtUserFilter jwtUserFilter;
//     public WebSecurityConfig(JwtAuthConverter jwtAuthConverter) {
//         this.jwtAuthConverter = jwtAuthConverter;
//     }

//     @Bean
//     public WebSecurityCustomizer webSecurityCustomizer() {
//         return (web) -> {
//             web.ignoring().requestMatchers(
//                     HttpMethod.POST,
//                     "/webhook"
//             );
//             web.ignoring().requestMatchers(
//                     HttpMethod.GET,
//                     "/auth/callback/**",
//                     "/zalo_verifier*.html"
//             );
//             web.ignoring().requestMatchers(
//                             HttpMethod.OPTIONS
//                     )
//                     .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
//                             "/swagger-resources/**", "/swagger-ui.html","/actuator/prometheus/**,");
//         };
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(AbstractHttpConfigurer::disable)
//                 .authorizeHttpRequests(auth -> auth
//                         .anyRequest()
//                         .authenticated())
//                 .oauth2ResourceServer(oauth2 -> oauth2
//                         .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
//                 .sessionManagement(session -> session
//                         .sessionCreationPolicy(STATELESS));

//         return http.build();
//     }


//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.addAllowedOrigin("https://epas.ngvgroup.vn");
//         configuration.addAllowedOrigin("https://epas.dev.ngvgroup.vn");
//         configuration.addAllowedOrigin("https://epas.qtd.vn");
//         configuration.addAllowedOrigin("http://10.10.0.37:8000");
//         configuration.addAllowedOrigin("http://10.10.0.39:9120");
//         configuration.addAllowedOrigin("http://localhost:4207");
//         configuration.addAllowedOrigin("http://localhost:4200");
//         configuration.addAllowedOrigin("http://localhost:4301");
//         configuration.addAllowedOrigin("http://localhost:4304");
//         configuration.addAllowedOriginPattern("https://*.zdn.vn");
//         configuration.addAllowedOriginPattern("https://*.ngvgroup.vn");
//         configuration.addAllowedMethod("*");
//         configuration.addAllowedHeader("*");
//         configuration.setAllowCredentials(true);
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration);
//         return source;
//     }

//     @Bean
//     public CorsFilter corsFilter() {
//         return new CorsFilter(corsConfigurationSource());
//     }

// }
