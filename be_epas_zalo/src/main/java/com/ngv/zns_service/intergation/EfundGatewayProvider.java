package com.ngv.zns_service.intergation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
public class EfundGatewayProvider {

    private List<EfundGatewayAuthenticateResultModel> authenticateResultModels;
    private int maxRetry = 3;
    private final OptionManager optionManager;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EfundGatewayProvider(OptionManager optionManager, WebClient webClient) {
        this.optionManager = optionManager;
        this.webClient = webClient;
        this.objectMapper = new ObjectMapper();
        this.authenticateResultModels = new ArrayList<>();
    }

    // Public methods

    /**
     * Gọi Get request
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> getAsync(String url, String partnerCode) {
        return getAsync(url, partnerCode, 0);
    }

    /**
     * Gọi Get request với xác thực Bearer token
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> getAsyncAuthen(String url, String partnerCode) {
        return getAsyncAuthen(url, partnerCode, 0);
    }

    /**
     * Gọi Get request với xác thực Bearer token
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @param zmpToken    Token xác thực user Zalo Mini App
     * @return
     */
    public CompletableFuture<ClientResponse> getAsyncAuthen(String url, String partnerCode, String zmpToken) {
        return getAsyncAuthen(url, partnerCode, 0, zmpToken);
    }

    /**
     * Gọi Post request
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> postAsync(String url, String partnerCode, Object data) {
        return postAsync(url, partnerCode, data, 0);
    }

    /**
     * Gọi Post request với xác thực Bearer token
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> postAsyncAuthen(String url, String partnerCode, Object data) {
        return postAsyncAuthen(url, partnerCode, data, 0);
    }

    /**
     * Gọi Post request với xác thực Bearer token
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> postAsyncAuthen(String url, String partnerCode, Object data, String zmpToken) {
        return postAsyncAuthen(url, partnerCode, data, 0, zmpToken);
    }

    /**
     * Gọi Put request
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<ClientResponse> putAsync(String url, String partnerCode, Object data) {
        return putAsync(url, partnerCode, data, 0);
    }

    // Public methods trả về String response

    /**
     * Gọi Get request và trả về String
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<String> getStringAsync(String url, String partnerCode) {
        return getAsync(url, partnerCode)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Get request với xác thực và trả về String
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<String> getStringAsyncAuthen(String url, String partnerCode) {
        return getAsyncAuthen(url, partnerCode)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Get request với xác thực và trả về String
     *
     * @param url
     * @param partnerCode Mã đối tác
     * @param zmpToken    Token xác thực user Zalo Mini App
     * @return
     */
    public CompletableFuture<String> getStringAsyncAuthen(String url, String partnerCode, String zmpToken) {
        return getAsyncAuthen(url, partnerCode, zmpToken)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Post request và trả về String
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<String> postStringAsync(String url, String partnerCode, Object data) {
        return postAsync(url, partnerCode, data)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    /**
     * Gọi Put request và trả về String
     *
     * @param url
     * @param data        Dữ liệu truyền vào body
     * @param partnerCode Mã đối tác
     * @return
     */
    public CompletableFuture<String> putStringAsync(String url, String partnerCode, Object data) {
        return putAsync(url, partnerCode, data)
                .thenCompose(response -> response.bodyToMono(String.class).toFuture());
    }

    // Private methods
    private CompletableFuture<ClientResponse> getAsync(String url, String partnerCode, int retry) {
        Mono<ClientResponse> responseMono = webClient.get()
                .uri(url)
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    // Không cần Bearer token
                })
                .exchange()
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
                        return Mono.fromFuture(getAsync(url, partnerCode, retry + 1));
                    }
                    return Mono.error(ex);
                });

        return responseMono.toFuture();
    }

    private CompletableFuture<ClientResponse> postAsync(String url, String partnerCode, Object data, int retry) {
        Mono<ClientResponse> responseMono = webClient.post()
                .uri(url)
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(data)
                .exchange()
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
                        return Mono.fromFuture(postAsync(url, partnerCode, data, retry + 1));
                    }
                    return Mono.error(ex);
                });

        return responseMono.toFuture();
    }

    private CompletableFuture<ClientResponse> putAsync(String url, String partnerCode, Object data, int retry) {
        Mono<ClientResponse> responseMono = webClient.put()
                .uri(url)
                .headers(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(data)
                .exchange()
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
                        return Mono.fromFuture(putAsync(url, partnerCode, data, retry + 1));
                    }
                    return Mono.error(ex);
                });

        return responseMono.toFuture();
    }


//    private CompletableFuture<ClientResponse> getAsync(String url, String partnerCode, int retry) {
//        return authenticateAPIAsync(partnerCode)
//                .thenCompose(token -> {
//                    Mono<ClientResponse> responseMono = webClient.get()
//                            .uri(url)
//                            .headers(headers -> {
//                                headers.setBearerAuth(token);
//                                headers.setContentType(MediaType.APPLICATION_JSON);
//                            })
//                            .exchange()
//                            .timeout(Duration.ofSeconds(30))
//                            .onErrorResume(WebClientResponseException.class, ex -> {
//                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
//                                    deleteAuthenticateToken(partnerCode);
//                                    return Mono.fromFuture(getAsync(url, partnerCode, retry + 1));
//                                }
//                                return Mono.error(ex);
//                            });
//
//                    return responseMono.toFuture();
//                });
//    }
//
    private CompletableFuture<ClientResponse> getAsyncAuthen(
            String url, String partnerCode, int retry) {
        return getAsyncAuthen(url, partnerCode, retry, "");
    }

    private CompletableFuture<ClientResponse> getAsyncAuthen(String url, String partnerCode, int retry, String zmpToken) {
        return authenticateAPIAsync(partnerCode)
                .thenCompose(token -> {
                    Mono<ClientResponse> responseMono = webClient.get()
                            .uri(url)
                            .headers(headers -> {
                                headers.setBearerAuth(token);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                if (zmpToken != null && !zmpToken.isEmpty()) {
                                    headers.add("X-USERZMP-TOKEN", zmpToken); // ✅ thêm token xác thực user zalomini app
                                }
                            })
                            .exchange()
                            .timeout(Duration.ofSeconds(30))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
                                    deleteAuthenticateToken(partnerCode);
                                    return Mono.fromFuture(getAsyncAuthen(url, partnerCode, retry + 1, zmpToken));
                                }
                                return Mono.error(ex);
                            });

                    return responseMono.toFuture();
                });
    }

    private CompletableFuture<ClientResponse> postAsyncAuthen(
            String url, String partnerCode, Object data, int retry) {
        return postAsyncAuthen(url, partnerCode, data, retry, "");
    }

    private CompletableFuture<ClientResponse> postAsyncAuthen(String url, String partnerCode, Object data, int retry, String zmpToken) {
        return authenticateAPIAsync(partnerCode)
                .thenCompose(token -> {
                    Mono<ClientResponse> responseMono = webClient.post()
                            .uri(url)
                            .headers(headers -> {
                                headers.setBearerAuth(token);
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                if (zmpToken != null && !zmpToken.isEmpty()) {
                                    headers.add("X-USERZMP-TOKEN", zmpToken); // ✅ thêm token xác thực user zalomini app
                                }
                            })
                            .bodyValue(data)
                            .exchange()
                            .timeout(Duration.ofSeconds(30))
                            .onErrorResume(WebClientResponseException.class, ex -> {
                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
                                    deleteAuthenticateToken(partnerCode);
                                    return Mono.fromFuture(postAsyncAuthen(url, partnerCode, data, retry + 1, zmpToken));
                                }
                                return Mono.error(ex);
                            });

                    return responseMono.toFuture();
                });
    }
//
//    private CompletableFuture<ClientResponse> putAsync(String url, String partnerCode, Object data, int retry) {
//        return authenticateAPIAsync(partnerCode)
//                .thenCompose(token -> {
//                    Mono<ClientResponse> responseMono = webClient.put()
//                            .uri(url)
//                            .headers(headers -> {
//                                headers.setBearerAuth(token);
//                                headers.setContentType(MediaType.APPLICATION_JSON);
//                            })
//                            .bodyValue(data)
//                            .exchange()
//                            .timeout(Duration.ofSeconds(30))
//                            .onErrorResume(WebClientResponseException.class, ex -> {
//                                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED && retry < maxRetry) {
//                                    deleteAuthenticateToken(partnerCode);
//                                    return Mono.fromFuture(putAsync(url, partnerCode, data, retry + 1));
//                                }
//                                return Mono.error(ex);
//                            });
//
//                    return responseMono.toFuture();
//                });
//    }

    /**
     * Thực hiện lấy Token, nếu chưa có hoặc hết hạn sẽ Gen token mới
     *
     * @param partnerCode Mã đối tác
     * @return
     */
    private CompletableFuture<String> authenticateAPIAsync(String partnerCode) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (authenticateResultModels == null) {
                    authenticateResultModels = new ArrayList<>();
                }

                // Remove expired tokens
                authenticateResultModels.removeIf(k -> k.getExpireAt().isBefore(LocalDateTime.now()));

                // Find existing valid token
                EfundGatewayAuthenticateResultModel authenticateResultModel =
                        authenticateResultModels.stream()
                                .filter(k -> k.getPartnerCode().equals(partnerCode))
                                .findFirst()
                                .orElse(null);

                if (authenticateResultModel != null) {
                    return authenticateResultModel.getAccessToken();
                }

                // Get new token
                EfundGatewayAuthenticateModel authenticateModel = getAuthenticateModel(partnerCode);
                String url = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_URL_GET_TOKEN);

                // Kiểm tra các trường bắt buộc
                if (authenticateModel.getUserNameOrEmailAddress() == null || authenticateModel.getUserNameOrEmailAddress().isEmpty()) {
                    throw new RuntimeException("Username is null or empty for partnerCode: " + partnerCode);
                }
                if (authenticateModel.getPassword() == null || authenticateModel.getPassword().isEmpty()) {
                    throw new RuntimeException("Password is null or empty for partnerCode: " + partnerCode);
                }

                try {
                    // Gọi API lấy token với headers đúng như curl
                    String result = webClient.post()
                            .uri(url)
                            .header("accept", "text/plain")
                            .contentType(MediaType.valueOf("application/json-patch+json"))
                            .bodyValue(authenticateModel)
                            .retrieve()
                            .bodyToMono(String.class)
                            .timeout(Duration.ofSeconds(30))
                            .block();

                    if (result == null || result.isEmpty()) {
                        throw new RuntimeException("Authentication response is null or empty");
                    }

                    // Parse token từ response
                    JsonNode response = objectMapper.readTree(result);
                    if (!response.has("result") || !response.get("result").has("accessToken")) {
                        throw new RuntimeException("Invalid response format, accessToken not found");
                    }

                    String accessToken = response.get("result").get("accessToken").asText();
                    int expireInSeconds = response.get("result").has("expireInSeconds")
                        ? response.get("result").get("expireInSeconds").asInt()
                        : 3600;

                    if (accessToken == null || accessToken.isEmpty()) {
                        throw new RuntimeException("Access token is null or empty in response");
                    }

                    authenticateResultModel = new EfundGatewayAuthenticateResultModel();
                    authenticateResultModel.setPartnerCode(partnerCode);
                    authenticateResultModel.setAccessToken(accessToken);
                    authenticateResultModel.setExpireInSeconds(expireInSeconds);
                    authenticateResultModel.setExpireAt(LocalDateTime.now().plusSeconds(expireInSeconds));
                    authenticateResultModels.add(authenticateResultModel);

                    return accessToken;
                } catch (WebClientResponseException wcEx) {
                    throw new RuntimeException("API error: " + wcEx.getStatusCode(), wcEx);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to authenticate", e);
            }
        });
    }

    /**
     * Xóa Token của đối tác
     *
     * @param partnerCode Mã đối tác
     */
    private void deleteAuthenticateToken(String partnerCode) {
        authenticateResultModels.removeIf(k -> k.getPartnerCode().equals(partnerCode));
    }

    private EfundGatewayAuthenticateModel getAuthenticateModel(String partnerCode) {
        try {
            EfundGatewayAuthenticateModel model = new EfundGatewayAuthenticateModel();
            String username = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_AUTHENTICATION_USER);
            String password = optionManager.getSettingValueForApplicationAsync(partnerCode, OptionConst.EFUND_GATEWAY_AUTHENTICATION_PASS);

            model.setUserNameOrEmailAddress(username);
            model.setPassword(password);
            return model;
        } catch (Exception e) {
            log.error("Failed to get authenticate model: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get authenticate model", e);
        }
    }
}

