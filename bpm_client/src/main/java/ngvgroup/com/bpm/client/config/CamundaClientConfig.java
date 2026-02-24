package ngvgroup.com.bpm.client.config; // Đổi package theo đúng project của bạn

import org.camunda.bpm.client.interceptor.ClientRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class CamundaClientConfig {

    @Value("${security.keycloak.token-uri}")
    String tokenUri;
    @Value("${security.keycloak.client-id}")
    String clientId;
    @Value("${security.keycloak.client-secret}")
    String clientSecret;

    // --- 2. Tự tạo ClientRegistrationRepository (Thay thế cấu hình trong YAML) ---
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Chúng ta tạo một client tên là "camunda-worker" bằng code
        ClientRegistration registration = ClientRegistration.withRegistrationId("camunda-worker")
                .tokenUri(tokenUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // Bắt buộc dòng này
                .build();

        return new InMemoryClientRegistrationRepository(registration);
    }

    // --- 3. Manager quản lý việc lấy Token ---
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(authorizedClientProvider);
        return manager;
    }

    // --- 4. Interceptor chèn Token vào Header ---
    @Bean
    public ClientRequestInterceptor interceptor(OAuth2AuthorizedClientManager manager) {
        return context -> {
            // "camunda-worker" phải khớp với tên đặt trong hàm withRegistrationId ở bước 2
            OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                    .withClientRegistrationId("camunda-worker")
                    .principal("external-task-client-service")
                    .build();

            // Gọi Spring Security để lấy Token
            try {
                OAuth2AuthorizedClient client = manager.authorize(request);
                if (client != null && client.getAccessToken() != null) {
                    context.addHeader("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
                }
            } catch (Exception e) {
                // Log lỗi nếu không lấy được token
                log.error("Không thể lấy Token: " + e.getMessage());
            }
        };
    }
}
