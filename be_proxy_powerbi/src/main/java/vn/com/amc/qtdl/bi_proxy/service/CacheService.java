package vn.com.amc.qtdl.bi_proxy.service;

import vn.com.amc.qtdl.bi_proxy.dto.ReportDto;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheService {

    private final Cache<String, String> usernameCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    private final Cache<String, Long> tokenExpCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();


    private final Cache<String, List<ReportDto>> reportCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();


    public void cacheUsername(String token, String username) {
        usernameCache.put(token, username);
    }

    public String getUsername(String token) {
        return usernameCache.getIfPresent(token);
    }

    public void cacheTokenExpired(String token, Long exp) {
        tokenExpCache.put(token, exp);
    }

    public Long getTokenExpired(String token) {
        return tokenExpCache.getIfPresent(token);
    }


    public void cacheReports(List<ReportDto> reports) {
        reportCache.put("reports", reports);
    }

    public List<ReportDto> getCachedReports() {
        return reportCache.getIfPresent("reports");
    }
}
