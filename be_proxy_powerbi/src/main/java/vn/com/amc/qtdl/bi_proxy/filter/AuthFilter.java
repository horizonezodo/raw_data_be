package vn.com.amc.qtdl.bi_proxy.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.com.amc.qtdl.bi_proxy.service.ReportPermissionService;
import vn.com.amc.qtdl.bi_proxy.service.CacheService;
import vn.com.amc.qtdl.bi_proxy.service.ScriptService;
import vn.com.amc.qtdl.bi_proxy.utils.JwtParser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final ReportPermissionService authValidationService;
    private final ScriptService scriptService;
    private final CacheService cacheService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Nếu là path token, trả về script
        if (path.contains("/token")) {
            scriptService.sendScriptResponse(response);
            return;
        }

        if (!path.startsWith("/Reports/powerbi/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String method = request.getMethod();
        String query = request.getQueryString();
        String fullPath = path + (query != null ? "?" + query : "");

        log.info("{} - {}", method, fullPath);

        // Nếu path được whitelist
        if (authValidationService.isWhitelistedPath(fullPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token từ cookie
        String token = getTokenFromCookie(request);

        if (isTokenExpired(token)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        // Kiểm tra Accept header
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        boolean containsTextHtml = acceptHeader != null &&
                Arrays.stream(acceptHeader.split(","))
                        .map(String::trim)
                        .anyMatch(type -> type.equalsIgnoreCase("text/html"));

        if (containsTextHtml) {
            String requestUrl = request.getRequestURL().toString();

            if (!authValidationService.validateAcceptHTML(requestUrl, token)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }
        }

        String referer = request.getHeader(HttpHeaders.REFERER);
        boolean valid = authValidationService.validateTokenAndReferer(token, referer);

        if (!valid) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

        filterChain.doFilter(request, response);
    }



    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        Optional<Cookie> tokenCookie = Arrays.stream(request.getCookies())
                .filter(c -> "Token".equals(c.getName()))
                .findFirst();
        return tokenCookie.map(Cookie::getValue).orElse(null);
    }

    private boolean isTokenExpired(String token) {
        if (token != null) {
            Long exp = cacheService.getTokenExpired(token);
            if (exp == null) {
                exp = JwtParser.getExpirationTime(token);
                if (exp != null && exp != -1) {
                    cacheService.cacheTokenExpired(token, exp);
                }
            }
            return exp != null && exp < System.currentTimeMillis() / 1000;
        }
        return true;
    }
}
