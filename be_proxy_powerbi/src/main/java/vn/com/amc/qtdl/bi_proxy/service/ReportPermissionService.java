package vn.com.amc.qtdl.bi_proxy.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import vn.com.amc.qtdl.bi_proxy.configs.WhitelistProperties;
import vn.com.amc.qtdl.bi_proxy.dto.ReportDto;
import vn.com.amc.qtdl.bi_proxy.repository.ResourceMappingRepository;
import vn.com.amc.qtdl.bi_proxy.utils.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportPermissionService {
    private final WhitelistProperties whitelistProperties;
    private final CacheService cacheService;
    private final ResourceMappingRepository resourceMappingRepository;

    @Value("${app.url-bi}")
    private String urlBI;

    @Value("${app.url-portal}")
    private String urlPortal;

    public boolean isWhitelistedPath(String fullPath) {
        List<String> whitelistPaths = whitelistProperties.getPaths();
        String whitelistPattern = whitelistProperties.getPattern();
        if (whitelistPaths != null) {
            for (String whitePath : whitelistPaths) {
                if (fullPath.startsWith(whitePath)) {
                    return true;
                }
            }
        }

        return whitelistPattern != null && fullPath.matches(whitelistPattern);
    }

    /**
     * Validate token và referer URL
     */
    public boolean validateTokenAndReferer(String token, String referer) {
        // Nếu referer hợp lệ trong whitelist
        if (isAllowedReferer(referer)) return true;

        String reportName = extractReportName(referer);
        if (token == null || reportName == null) {
            return false;
        }

        try {
            // Giải mã JWT (nếu bạn có jwtDecoder đồng bộ hoặc decode thủ công)
            DecodedJWT jwt = JWT.decode(token);
            // Kiểm tra quyền user truy cập report
            return hasUserAccess(token, reportName);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAllowedReferer(String referer) {
        return referer == null || referer.equals(urlBI) || referer.equals(urlPortal);
    }

    /**
     * Trích xuất report name từ referer URL
     */
    private String extractReportName(String referer) {
        if (referer != null && referer.startsWith(urlBI)) {
            String path = referer.replace(urlBI, "");
            if (path.startsWith("Reports/powerbi/")) {
                path = path.substring(16);
            }
            return path.split("[?]")[0];
        }
        return null;
    }


    /**
     * Kiểm tra quyền truy cập của user
     */
    private boolean hasUserAccess(String token, String reportName) {
        // Lấy username từ cache hoặc từ JWT
        final String cachedUsername = cacheService.getUsername(token);
        final String username = ((cachedUsername != null) ? cachedUsername : JwtParser.getUserName(token));

        // Nếu username chưa có trong cache, lưu lại cache
        if (cachedUsername == null) {
            cacheService.cacheUsername(token, username);
        }

        // Lấy danh sách report theo user từ cache
        List<ReportDto> cachedReports = cacheService.getCachedReports();

        // Nếu cache có dữ liệu, kiểm tra quyền trong cache
        if (cachedReports == null || cachedReports.isEmpty()) {
            cachedReports = resourceMappingRepository.findAllReport(username);
            cacheService.cacheReports(cachedReports);
        }

        // Kiểm tra quyền trong danh sách
        boolean isAuthorized = cachedReports.stream()
                .anyMatch(report -> {
                    String url = report.getReportSource();
                    if (url == null) return false;

                    int index = url.indexOf("/Reports/powerbi/");
                    if (
//                            index >= 0 &&
                            url.length() > index + "/Reports/powerbi/".length()
                    ) {
//                        String partAfterViews = url.substring(index + "powerbi/".length());
//                        return Objects.equals(partAfterViews, reportName);
                        return Objects.equals(url, reportName);
                    }
                    return false;
                });


        if (isAuthorized) {
            return true;
        }
        log.warn("Token không có quyền truy cập vào báo cáo: {}", reportName);
        return false;
    }

    /**
     * Validate token cho HTML request
     */
    public boolean validateAcceptHTML(String url, String token) {
        if(url.equals(urlBI)) {
            return true;
        }
        String reportNameInUrl = extractReportNameInUrl(url);
        return hasUserAccess(token, reportNameInUrl);
    }

    /**
     * Trích xuất report name từ URL
     */
    private String extractReportNameInUrl(String path) {
        if (path != null && path.contains("/Reports/powerbi/")) {
            path = path.substring(path.indexOf("/Reports/powerbi/") + 17);
            return path.split("\\?")[0];
        }
        return null;
    }


}
