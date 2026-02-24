package vn.com.amc.qtdl.tableau_proxy.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.amc.qtdl.tableau_proxy.models.dtos.ReportDto;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    @Value("${cache.report.expire-minutes}")
    private long timeExpire;

    private Cache<String, List<ReportDto>> reportCache;

    @PostConstruct
    public void init() {
        reportCache = Caffeine.newBuilder()
                .expireAfterWrite(timeExpire, TimeUnit.MINUTES)
                .build();
    }

    public void clearAllReports() {
        reportCache.invalidateAll();
    }

    public void cacheReports(String userId, List<ReportDto> reports) {
        reportCache.put(userId, reports);
    }

    public List<ReportDto> getCachedReports(String userId) {
        return reportCache.getIfPresent(userId);
    }
}

