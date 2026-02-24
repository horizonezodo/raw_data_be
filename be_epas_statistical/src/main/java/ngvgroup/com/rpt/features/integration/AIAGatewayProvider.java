package ngvgroup.com.rpt.features.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.extern.log4j.Log4j2;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static ngvgroup.com.rpt.core.constant.VariableConstants.AUTHENTICATION_FAILED;
import static ngvgroup.com.rpt.core.constant.VariableConstants.AUTHENTICATION_MODEL_FAILED;

@Log4j2
@Component
public class AIAGatewayProvider {

    private List<AIAGatewayAuthenticateResultModel> authenticateResultModels;
    private static final int MAX_RETRY = 3;
    private final OptionManager optionManager;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private Integer cachedTimeoutSeconds = null;

    @Autowired
    public AIAGatewayProvider(OptionManager optionManager, WebClient webClient) {
        this.optionManager = optionManager;
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
        this.authenticateResultModels = new ArrayList<>();
    }

    // Public methods

    /**
     * Gọi Get request với xác thực Bearer token
     *
     */
    public CompletableFuture<ClientResponse> getAsync(String url) {
        return getAsync(url, 0);
    }

    /**
     * Gọi Post request với xác thực Bearer token
     *
     * @param data Dữ liệu truyền vào body
     */
    public CompletableFuture<ClientResponse> postAsync(String url, Object data) {
        return postAsync(url, data, 0);
    }

    /**
     * Gọi Put request với xác thực Bearer token
     *
     * @param data Dữ liệu truyền vào body
     */
    public CompletableFuture<ClientResponse> putAsync(String url, Object data) {
        return putAsync(url, data, 0);
    }

    // Public methods trả về String response

    /**
     * Gọi Get request với xác thực Bearer token và trả về String
     *
     */
    public CompletableFuture<String> getStringAsync(String url) {
        return getAsync(url)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Post request với xác thực Bearer token và trả về String
     *
     * @param data Dữ liệu truyền vào body
     */
    public CompletableFuture<String> postStringAsync(String url, Object data) {
        return postAsync(url, data)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Put request với xác thực Bearer token và trả về String
     *
     * @param data Dữ liệu truyền vào body
     */
    public CompletableFuture<String> putStringAsync(String url, Object data) {
        return putAsync(url, data)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    // Private methods
    private int getTimeoutSeconds() {
        if (cachedTimeoutSeconds != null) {
            return cachedTimeoutSeconds;
        }
        try {
            String value = optionManager.getSettingValueForApplicationAsync("AIA_GATEWAY_TIMEOUT_SECONDS");
            if (value != null && !value.isEmpty()) {
                cachedTimeoutSeconds = Integer.parseInt(value);
            } else {
                cachedTimeoutSeconds = 180;
            }
        } catch (Exception ignored) {
            cachedTimeoutSeconds = 180;
        }
        return cachedTimeoutSeconds;
    }

    private CompletableFuture<ClientResponse> getAsync(String url, int retry) {
        int timeoutSeconds = getTimeoutSeconds();
        return authenticateAPIAsync()
                .thenCompose(token -> {
                    Mono<ClientResponse> responseMono = webClient.get()
                            .uri(url)
                            .headers(headers -> {
                                headers.setBearerAuth(token);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                            })
                            .exchangeToMono(Mono::just)
                            .timeout(Duration.ofSeconds(timeoutSeconds))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < MAX_RETRY) {
                                    deleteAuthenticateToken();
                                    return Mono.fromFuture(getAsync(url, retry + 1));
                                }
                                return Mono.error(ex);
                            });

                    return responseMono.toFuture();
                });
    }

    private CompletableFuture<ClientResponse> postAsync(String url, Object data, int retry) {
        int timeoutSeconds = getTimeoutSeconds();
        return authenticateAPIAsync()
                .thenCompose(token -> {
                    Mono<ClientResponse> responseMono = webClient.post()
                            .uri(url)
                            .headers(headers -> {
                                headers.setBearerAuth(token);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                            })
                            .bodyValue(data)
                            .exchangeToMono(Mono::just)
                            .timeout(Duration.ofSeconds(timeoutSeconds))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < MAX_RETRY) {
                                    deleteAuthenticateToken();
                                    return Mono.fromFuture(postAsync(url, data, retry + 1));
                                }
                                return Mono.error(ex);
                            });

                    return responseMono.toFuture();
                });
    }

    private CompletableFuture<ClientResponse> putAsync(String url, Object data, int retry) {
        int timeoutSeconds = getTimeoutSeconds();
        return authenticateAPIAsync()
                .thenCompose(token -> {
                    Mono<ClientResponse> responseMono = webClient.put()
                            .uri(url)
                            .headers(headers -> {
                                headers.setBearerAuth(token);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                            })
                            .bodyValue(data)
                            .exchangeToMono(Mono::just)
                            .timeout(Duration.ofSeconds(timeoutSeconds))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < MAX_RETRY) {
                                    deleteAuthenticateToken();
                                    return Mono.fromFuture(putAsync(url, data, retry + 1));
                                }
                                return Mono.error(ex);
                            });

                    return responseMono.toFuture();
                });
    }

    /**
     * Thực hiện lấy Token, nếu chưa có hoặc hết hạn sẽ Gen token mới
     *
     */
    private CompletableFuture<String> authenticateAPIAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                authenticateResultModels = Optional.ofNullable(authenticateResultModels)
                        .orElseGet(ArrayList::new);

                // Remove expired tokens
                authenticateResultModels.removeIf(k -> k.getExpireAt().isBefore(LocalDateTime.now()));

                // Find existing valid token
                AIAGatewayAuthenticateResultModel authenticateResultModel =
                        authenticateResultModels.stream().findFirst().orElse(null);

                if (authenticateResultModel != null) {
                    return authenticateResultModel.getAccessToken();
                }

                // No token → call API
                return requestNewAccessToken();

            } catch (Exception e) {
                throw new BusinessException(StatisticalErrorCode.AUTHENTICATE_FAILED, e);
            }
        });
    }

    private String requestNewAccessToken() {
        AIAGatewayAuthenticateModel authenticateModel = getAuthenticateModel();
        String url = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_URL_GET_TOKEN);

        validateNotBlank(authenticateModel.getUserNameOrEmailAddress(), StatisticalErrorCode.IN_VALID_USERNAME);
        validateNotBlank(authenticateModel.getPassword(), StatisticalErrorCode.IN_VALID_PASSWORD);

        String basicAuth = Base64.getEncoder()
                .encodeToString((authenticateModel.getUserNameOrEmailAddress() + ":" + authenticateModel.getPassword()).getBytes());

        try {
            String result = webClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + basicAuth)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(getTimeoutSeconds()))
                    .block();

            if (result == null || result.isEmpty()) {
                throw new BusinessException(StatisticalErrorCode.AUTHENTICATION_RESPONSE_NULL);
            }

            JsonNode response = objectMapper.readTree(result);

            validateErrorCode(response);

            if (!response.has("data")) {
                throw new BusinessException(StatisticalErrorCode.INVALID_RESPONSE);
            }

            JsonNode dataNode = response.get("data");

            String accessToken = extractAccessToken(dataNode);
            int expireInSeconds = extractExpireSeconds(dataNode);

            AIAGatewayAuthenticateResultModel authenticateResultModel = new AIAGatewayAuthenticateResultModel();
            authenticateResultModel.setPartnerCode("DEFAULT");
            authenticateResultModel.setAccessToken(accessToken);
            authenticateResultModel.setExpireInSeconds(expireInSeconds);
            authenticateResultModel.setExpireAt(LocalDateTime.now().plusSeconds(expireInSeconds));

            authenticateResultModels.add(authenticateResultModel);

            return accessToken;

        } catch (WebClientResponseException wcEx) {
            throw new BusinessException(StatisticalErrorCode.API_ERROR, wcEx.getStatusCode(), wcEx);
        } catch (Exception ex) {
            throw new BusinessException(StatisticalErrorCode.AUTHENTICATE_FAILED, ex);
        }
    }

    private void validateNotBlank(String value, ErrorCode errorMessage) {
        if (value == null || value.isEmpty()) {
            throw new BusinessException(errorMessage);
        }
    }

    private void validateErrorCode(JsonNode response) {
        if (!response.has("error_code") || response.get("error_code").asInt() != 0) {
            String errorMessage = response.has("message")
                    ? response.get("message").asText()
                    : AUTHENTICATION_FAILED;

            throw new BusinessException(StatisticalErrorCode.AUTHENTICATE_FAILED, errorMessage);
        }
    }

    private String extractAccessToken(JsonNode dataNode) {
        if (!dataNode.has("access_token")) {
            throw new BusinessException(StatisticalErrorCode.INVALID_RESPONSE_ACCESS_TOKEN);
        }

        String accessToken = dataNode.get("access_token").asText();

        if (accessToken == null || accessToken.isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.ACCESS_TOKEN_IS_NULL_OR_EMPTY);
        }

        return accessToken;
    }

    private int extractExpireSeconds(JsonNode dataNode) {
        return dataNode.has("expires_in")
                ? dataNode.get("expires_in").asInt()
                : 3600;
    }

    private AIAGatewayAuthenticateModel getAuthenticateModel() {
        try {
            AIAGatewayAuthenticateModel model = new AIAGatewayAuthenticateModel();
            String username = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_AUTHENTICATION_USER);
            String password = optionManager.getSettingValueForApplicationAsync(OptionConst.AIA_GATEWAY_AUTHENTICATION_PASS);

            model.setUserNameOrEmailAddress(username);
            model.setPassword(password);
            return model;
        } catch (Exception e) {
            log.error(AUTHENTICATION_MODEL_FAILED, e.getMessage(), e);
            throw new BusinessException(StatisticalErrorCode.AUTHENTICATE_MODEL_FAILED, e);
        }
    }

    /**
     * Xóa token đã hết hạn hoặc không hợp lệ
     */
    private void deleteAuthenticateToken() {
        if (authenticateResultModels != null) {
            authenticateResultModels.clear();
        }
    }

}