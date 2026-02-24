package com.ngvgroup.bpm.core.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.security.CompositeBearerTokenResolver;
import com.ngvgroup.bpm.core.security.PublicSkippingBearerTokenResolver;
import com.ngvgroup.bpm.core.security.BpmHttpSecurityCustomizer;
import com.ngvgroup.bpm.core.security.jwt.JwtAuthConverter;
import com.ngvgroup.bpm.core.security.jwt.JwtAuthConverterProperties;
import com.ngvgroup.bpm.core.security.jwt.JwtAuthenticationEntryPoint;
import com.ngvgroup.bpm.core.security.permission.service.PermissionService;
import com.ngvgroup.bpm.core.security.ws.WsProtocolBearerTokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ============================
 * BPM Core Security AutoConfig
 * ============================
 * Mục tiêu:
 * - Giữ nguyên @PreAuthorize("hasRole('permission_code')")
 * - Switch theo YAML:
 *   + bpm.core.security.permission.use-db=false -> authorities từ Keycloak realm_access.roles (JwtAuthConverter)
 *   + bpm.core.security.permission.use-db=true  -> authorities từ Redis snapshot (PermissionService)
 *
 * Lưu ý:
 * - DB/Redis mode bạn chốt: USER_ID = sub (Keycloak userId)
 * - Lib/core CHỈ ĐỌC Redis (không query DB)
 */
@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({
        JwtAuthConverterProperties.class,
        BpmCoreProperties.class
})
public class BpmCoreSecurityAutoConfiguration {

    private final BpmCoreProperties properties;

    // ---------------------------
    // 1) JWT converter + entry point
    // ---------------------------

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter(
            JwtAuthConverterProperties props,
            BpmCoreProperties bpmCoreProperties,
            ObjectProvider<PermissionService> permissionServiceProvider
    ) {
        boolean useDb = bpmCoreProperties.getSecurity() != null
                && bpmCoreProperties.getSecurity().getPermission() != null
                && bpmCoreProperties.getSecurity().getPermission().isUseDb();

        if (!useDb) {
            // Keycloak mode: giữ nguyên convert roles từ token
            return new JwtAuthConverter(props);
        }

        // DB/Redis mode: build authorities từ PermissionService (Redis snapshot)
        PermissionService permissionService = permissionServiceProvider.getIfAvailable();
        return new DbPermissionJwtAuthConverter(permissionService);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    // ---------------------------
    // 2) Public matcher dùng chung
    // ---------------------------

    @Bean
    public RequestMatcher bpmPublicMatcher(BpmCoreProperties bpmProps, HandlerMappingIntrospector introspector) {
        return buildPublicMatcher(bpmProps, introspector);
    }

    // ---------------------------
    // 3) BearerTokenResolver (compose)
    // ---------------------------

    @Bean
    public BearerTokenResolver bearerTokenResolver(RequestMatcher bpmPublicMatcher) {

        BearerTokenResolver defaultResolver = new DefaultBearerTokenResolver();
        BearerTokenResolver wsResolver = new WsProtocolBearerTokenResolver();

        BearerTokenResolver composite = new CompositeBearerTokenResolver(List.of(defaultResolver, wsResolver));
        return new PublicSkippingBearerTokenResolver(bpmPublicMatcher, composite);
    }

    // ---------------------------
    // 4) SecurityFilterChain
    // ---------------------------

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain bpmSecurityFilterChain(
            HttpSecurity http,
            Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            BpmCoreProperties bpmProps,
            BearerTokenResolver bearerTokenResolver,
            ObjectProvider<List<BpmHttpSecurityCustomizer>> customizersProvider
    ) throws Exception {

        String[] publicPaths = (bpmProps.getWeb().getPublicPaths() == null)
                ? new String[0]
                : bpmProps.getWeb().getPublicPaths().toArray(new String[0]);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(publicPaths).permitAll()
                        .anyRequest().authenticated()
                );

        http.oauth2ResourceServer(oauth2 -> oauth2
                .bearerTokenResolver(bearerTokenResolver)
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );

        // Allow other BPM modules to add filters / matchers without replacing the chain.
        List<BpmHttpSecurityCustomizer> customizers = customizersProvider.getIfAvailable();
        if (customizers != null) {
            for (BpmHttpSecurityCustomizer c : customizers) {
                if (c != null) {
                    c.customize(http);
                }
            }
        }
        return http.build();
    }


    /**
     * Build matcher cho publicPaths bằng MvcRequestMatcher
     */
    private RequestMatcher buildPublicMatcher(BpmCoreProperties bpmProps, HandlerMappingIntrospector introspector) {
        List<String> publicPaths = bpmProps.getWeb().getPublicPaths();
        if (publicPaths == null || publicPaths.isEmpty()) {
            return request -> false;
        }

        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
        List<RequestMatcher> matchers = new ArrayList<>();

        for (String p : publicPaths) {
            matchers.add(mvc.pattern(p)); // ✅ không duplicate
        }

        return new OrRequestMatcher(matchers);
    }

    // ---------------------------
    // 5) CORS
    // ---------------------------

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        List<String> origins = properties.getCors().getAllowedOrigins();
        if (origins != null && !origins.isEmpty()) {
            configuration.setAllowedOriginPatterns(origins);
        }

        configuration.setAllowedMethods(properties.getCors().getAllowedMethods());
        configuration.setAllowedHeaders(properties.getCors().getAllowedHeaders());
        configuration.setExposedHeaders(properties.getCors().getExposedHeaders());
        configuration.setAllowCredentials(properties.getCors().isAllowCredentials());
        configuration.setMaxAge(properties.getCors().getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ---------------------------
    // 6) WebSecurityCustomizer
    // ---------------------------

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(BpmCoreProperties bpmProps) {
        String[] ignorePaths = bpmProps.getWeb().getIgnoringPaths().stream()
                .filter(p -> !p.startsWith("/api/"))
                .toArray(String[]::new);

        return web -> web.ignoring().requestMatchers(ignorePaths);
    }

    // =====================================================
    // 7) Jwt converter for DB/Redis permission mode
    // =====================================================

    /**
     * DB/Redis Permission mode converter:
     * - userId = sub
     * - authorities = ROLE_<permission_code> từ PermissionService (Redis snapshot)
     */
    static class DbPermissionJwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        private final PermissionService permissionService;

        DbPermissionJwtAuthConverter(PermissionService permissionService) {
            this.permissionService = permissionService;
        }

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {

            // ✅ userId = sub (đúng với perm:user:{sub})
            String userId = jwt.getClaimAsString("sub");
            if (userId == null || userId.isBlank()) {
                userId = jwt.getSubject();
            }

            List<String> groups = safeList(jwt.getClaimAsStringList("groups"));

            Set<String> perms = (permissionService == null)
                    ? Set.of()
                    : permissionService.findPermissionCodes(userId, groups);

            Collection<GrantedAuthority> authorities = perms.stream()
                    .filter(p -> p != null && !p.isBlank())
                    .map(p -> new SimpleGrantedAuthority("ROLE_" + p.trim()))
                    .collect(Collectors.toSet());

            // principalName: dùng sub để đồng bộ cache + DB USER_ID=sub
            return new JwtAuthenticationToken(jwt, authorities, userId);
        }

        private static <T> List<T> safeList(List<T> in) {
            return (in != null) ? in : List.of();
        }
    }
}
