package ngvgroup.com.rpt.features.powerbi_cloud.service;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IClientCredential;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.powerbi_cloud.dto.EmbedConfigDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PowerBiService extends BaseStoredProcedureService {

    private final String clientId;
    private final String clientSecret;
    private final String tenantId;
    private final String authority;
    private final String scope;
    private final String powerBiApiUrl;

    public PowerBiService(
            @Value("${azure.activedirectory.client-id}") String clientId,
            @Value("${azure.activedirectory.client-secret}") String clientSecret,
            @Value("${azure.activedirectory.tenant-id}") String tenantId,
            @Value("${azure.activedirectory.authority}") String authority,
            @Value("${azure.activedirectory.scope}") String scope,
            @Value("${powerbi.api.url}") String powerBiApiUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tenantId = tenantId;
        this.authority = authority;
        this.scope = scope;
        this.powerBiApiUrl = powerBiApiUrl;
    }

    public EmbedConfigDTO getEmbedConfig(String reportSource) throws BusinessException {
        if (reportSource == null || reportSource.trim().isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.POWER_BI_CLOUD_REPORT_SOURCE_NOTFOUND);
        }

        String[] parts = reportSource.split("/");

        if (parts.length < 4) {
            throw new BusinessException(StatisticalErrorCode.POWER_BI_CLOUD_REPORT_SOURCE_INVALID);
        }

        String workspaceId = parts[1];
        String reportId = parts[3];

        // 1. Lấy Access Token
        String accessToken = getAzureAccessToken();

        // 2. Lấy Dataset ID từ Report
        String datasetId = getDatasetIdFromReport(accessToken, workspaceId, reportId);

        // 3. Lấy roles từ Dataset
        List<String> roles = getDatasetRoles(accessToken, datasetId);

        // 4. Lấy userName hiện tại từ JWT
        String userName = getCurrentUserName();

        // 5. Sinh embed token với effective identity
        return generateEmbedToken(accessToken, workspaceId, reportId, datasetId, userName, roles);
    }

    private String getAzureAccessToken() {
        try {
            IClientCredential credential = ClientCredentialFactory.createFromSecret(clientSecret);
            ConfidentialClientApplication app = ConfidentialClientApplication.builder(clientId, credential)
                    .authority(authority + tenantId)
                    .build();
            ClientCredentialParameters parameters = ClientCredentialParameters.builder(
                    Collections.singleton(scope)).build();
            return app.acquireToken(parameters).join().accessToken();
        } catch (Exception e) {
            log.error("Failed to acquire Azure Access Token", e);
            throw new BusinessException(
                    StatisticalErrorCode.POWER_BI_CLOUD_AUTH_FAILED,
                    e);
        }
    }

    /**
     * Lấy Dataset ID từ Power BI Report API
     */
    private String getDatasetIdFromReport(String accessToken, String workspaceId, String reportId) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = String.format("%s/groups/%s/reports/%s", powerBiApiUrl, workspaceId, reportId);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("datasetId")) {
                String datasetId = (String) responseBody.get("datasetId");
                log.info("Retrieved dataset ID: {} for report: {}", datasetId, reportId);
                return datasetId;
            }
            throw new BusinessException(ErrorCode.NOT_FOUND, "Không tìm thấy datasetId trong Power BI Report");
        } catch (HttpClientErrorException e) {
            log.error("Failed to get dataset ID from Power BI: Code={}, Body={}", e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "Lỗi khi lấy thông tin Report từ Power BI: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("System Error when getting dataset ID:", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi lấy thông tin Report");
        }
    }

    /**
     * Lấy danh sách Roles từ JWT token của user hiện tại
     * Filter các role có prefix "MSBI_" và remove prefix để dùng cho Power BI RLS
     */
    private List<String> getDatasetRoles(String accessToken, String datasetId) {
        // Lấy JWT từ SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            // Lấy realm_access.roles từ JWT
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess != null) {
                @SuppressWarnings("unchecked")
                Collection<String> roles = (Collection<String>) realmAccess.get("roles");

                if (roles != null) {
                    // Filter và transform roles có prefix "MSBI_"
                    List<String> powerBiRoles = roles.stream()
                            .filter(role -> role.startsWith("MSBI_"))
                            .map(role -> role.substring(5)) // Remove "MSBI_" prefix
                            .collect(Collectors.toList());

                    if (!powerBiRoles.isEmpty()) {
                        log.info("Found Power BI roles from JWT for dataset {}: {}", datasetId, powerBiRoles);
                        return powerBiRoles;
                    }
                }
            }
        }

        // Fallback nếu không tìm thấy MSBI_ roles
        log.warn("No MSBI_ roles found in JWT for dataset {}. Using default role 'R'.", datasetId);
        return List.of("");
    }

    private EmbedConfigDTO generateEmbedToken(String accessToken, String workspaceId, String reportId,
                                              String datasetId, String username, List<String> roles) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build request body với effective identity
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accessLevel", "View");

        // Thêm datasets array
        requestBody.put("datasets", List.of(Map.of("id", datasetId)));
        //
        // // Thêm identities array với username, roles và dataset
        Map<String, Object> identity = new HashMap<>();
        identity.put("username", username);
        identity.put("roles", roles);
        identity.put("datasets", List.of(datasetId));
        requestBody.put("identities", List.of(identity));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = String.format("%s/groups/%s/reports/%s/GenerateToken", powerBiApiUrl, workspaceId, reportId);

        log.info("Generating embed token for user: {}, dataset: {}, roles: {}", username, datasetId, roles);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                String token = (String) responseBody.get("token");
                String embedUrl = "https://app.powerbi.com/reportEmbed?reportId=" + reportId + "&groupId="
                        + workspaceId;
                log.info("Successfully generated embed token for user: {}", username);
                return new EmbedConfigDTO(token, embedUrl, reportId);
            }
        } catch (HttpClientErrorException e) {
            log.error("Power BI API Error: Code={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Lỗi từ Power BI: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("System Error when calling Power BI:", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi kết nối Power BI");
        }

        throw new BusinessException(ErrorCode.NOT_FOUND, "Không lấy được dữ liệu Token từ Power BI");
    }

}