package vn.com.amc.qtdl.tableau_proxy.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.com.amc.qtdl.tableau_proxy.configs.WhitelistProperties;
import vn.com.amc.qtdl.tableau_proxy.models.dtos.ReportDto;
import vn.com.amc.qtdl.tableau_proxy.repositories.RptReportRepository;
import vn.com.amc.qtdl.tableau_proxy.utilities.JwtParser;


import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthValidationService {
    private final WhitelistProperties whitelistProperties;

    @Value("${app.url-tableau}")
    private String urlTableau;

    @Value("${app.url-portal}")
    private String urlPortal;

    private final CacheService cacheService;
    private final RptReportRepository rptReportRepository;
    private final ReactiveJwtDecoder jwtDecoder;

    /**
     * Kiểm tra xem path có nằm trong danh sách được phép không
     */
    public boolean isWhitelistedPath(String fullPath) {
        if (fullPath == null || fullPath.isEmpty()) {
            return false;
        }

        // Tách phần path (bỏ query string nếu có)
        String pathWithoutQuery = fullPath.split("\\?")[0];

        List<String> whitelistPaths = whitelistProperties.getPaths();
        String whitelistPattern = whitelistProperties.getPattern();

        // Kiểm tra path nằm trong danh sách paths được whitelist
        if (whitelistPaths != null) {
            for (String whitePath : whitelistPaths) {
                if (pathWithoutQuery.startsWith(whitePath)) {
                    return true;
                }
            }
        }

        if (whitelistPattern != null && !whitelistPattern.isEmpty()) {
            if (pathWithoutQuery.matches(whitelistPattern)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Validate token và referer URL
     */
    public Mono<Boolean> validateTokenAndReferer(String token, String referer) {
        if (isAllowedReferer(referer)) return Mono.just(true);

        String reportName = extractReportName(referer);
        if (token == null || reportName == null) {
            return Mono.just(false);
        }

        return jwtDecoder.decode(token)
                .flatMap(jwt -> Mono.fromCallable(() -> hasUserAccess(token, reportName)))
                .onErrorReturn(false);

    }

    /**
     * Validate token cho HTML request
     */
    public boolean validateAcceptHTML(String url, String token) {
        if (url.equals(urlTableau)) {
            return true;
        }
        String reportNameInUrl = extractReportNameInUrl(url);
        return hasUserAccess(token, reportNameInUrl);
    }

    /**
     * Kiểm tra referer có được phép không
     */
    private boolean isAllowedReferer(String referer) {
        return referer == null || referer.equals(urlTableau) || referer.equals(urlPortal);
    }

    /**
     * Kiểm tra quyền truy cập của user
     */
    private boolean hasUserAccess(String token, String reportName) {
        String userId = JwtParser.getUserName(token);
        List<ReportDto> cachedReports = cacheService.getCachedReports(userId);

        if (cachedReports == null || cachedReports.isEmpty()) {
            cachedReports = rptReportRepository.findAllReport(userId);
            cacheService.cacheReports(userId, cachedReports);
        }

        boolean isAuthorized = cachedReports.stream()
                .anyMatch(report -> {
                    String normalizedUrl = normalizeReportName(report.getReportSource());
                    String normalizedName = normalizeReportName(reportName);
                    return Objects.equals(normalizedUrl, normalizedName);
                });


        if (isAuthorized) {
            return true;
        }
        log.warn("Token không có quyền truy cập vào báo cáo: {}", reportName);
        return false;
    }

    private String normalizeReportName(String path) {
        if (path == null) return null;
        int index = path.indexOf("views/");
        if (index >= 0) {
            path = path.substring(index + "views/".length());
        }

        int q = path.indexOf('?');
        if (q >= 0) {
            path = path.substring(0, q);
        }
        return path.trim();
    }


    /**
     * Trích xuất report name từ URL
     */
    private String extractReportNameInUrl(String path) {
        if (path != null && path.contains("/views/")) {
            path = path.substring(path.indexOf("/views/") + 7);
            return path.split("\\?")[0];
        }
        return null;
    }

    /**
     * Trích xuất report name từ referer URL
     */
    private String extractReportName(String referer) {
        if (referer != null && referer.startsWith(urlTableau)) {
            String path = referer.replace(urlTableau, "");
            if (path.startsWith("views/")) {
                path = path.substring(6);
            }
            return path.split("[?]")[0];
        }
        return null;
    }
}
