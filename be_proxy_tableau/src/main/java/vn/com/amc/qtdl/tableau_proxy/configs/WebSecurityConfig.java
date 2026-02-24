package vn.com.amc.qtdl.tableau_proxy.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    @Value("${oauth2.jwtPublicKey}")
    private String jwtPublicKey;

    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = getPublicKeyFromPEM(jwtPublicKey);
        return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
    }

    private RSAPublicKey getPublicKeyFromPEM(String pem) throws Exception {
        String cleanPem = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(cleanPem);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .headers(headers -> headers
                    .frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                .build();
    }


}
