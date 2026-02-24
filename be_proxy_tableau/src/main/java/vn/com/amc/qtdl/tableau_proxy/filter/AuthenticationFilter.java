package vn.com.amc.qtdl.tableau_proxy.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import vn.com.amc.qtdl.tableau_proxy.service.AuthValidationService;
import vn.com.amc.qtdl.tableau_proxy.service.CacheService;
import vn.com.amc.qtdl.tableau_proxy.service.ScriptService;
import vn.com.amc.qtdl.tableau_proxy.utilities.JwtParser;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final AuthValidationService authValidationService;
    private final ScriptService scriptService;
    private final CacheService cacheService;


    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String method = String.valueOf(request.getMethod());
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();

        String fullPath = path + (query != null ? "?" + query : "");

        log.info("{} - {}", method, fullPath);

        if (path.contains("/token")) {
            cacheService.clearAllReports();
            return scriptService.sendScriptResponse(exchange);
        }

        if (authValidationService.isWhitelistedPath(fullPath)) {
            return chain.filter(exchange);
        }

        String token = getCookie(request);

        if (isTokenExpired(token)) {
            return blockRequest(exchange);
        }

        String acceptHeader = request.getHeaders().getFirst(HttpHeaders.ACCEPT);


        boolean containsTextHtml = acceptHeader != null && Arrays.stream(acceptHeader.split(",")).map(String::trim).anyMatch(type -> type.equalsIgnoreCase("text/html"));

        if (containsTextHtml) {
            String requestUrl = request.getURI().toString();

            if (!authValidationService.validateAcceptHTML(requestUrl, token)) {
                return blockRequest(exchange);
            }

            String usernameFromToken = JwtParser.getUserName(token);

            MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI())
                    .build().getQueryParams();

            String usrnmParam = queryParams.getFirst("USRNM");

            if (usrnmParam != null) {
                if (!usrnmParam.equals(usernameFromToken)) {
                    return blockRequest(exchange);
                }
            } else {
                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(request.getURI())
                        .replaceQueryParam(":embed", "y")
                        .replaceQueryParam("USRNM", usernameFromToken);

                URI newUri = uriBuilder.build(true).toUri();

                ServerHttpRequest newRequest = request.mutate().uri(newUri).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

                return chain.filter(newExchange);
            }

    }


        String referer = request.getHeaders().getFirst(HttpHeaders.REFERER);
        return authValidationService.validateTokenAndReferer(token, referer).flatMap(isValid -> isValid ? chain.filter(exchange) : blockRequest(exchange));
    }


    private String getCookie(ServerHttpRequest request) {
        Optional<HttpCookie> tokenCookie = request.getCookies().getOrDefault("Token", List.of()).stream().findFirst();
        return tokenCookie.map(HttpCookie::getValue).orElse(null);
    }

    private Mono<Void> blockRequest(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public boolean isTokenExpired(String token) {
        if (token != null) {
            Long exp = JwtParser.getExpirationTime(token);
            return exp != null && exp < System.currentTimeMillis() / 1000;
        }
        return true;

    }


    @Override
    public int getOrder() {
        return -10;
    }
}


