package com.naas.admin_service.features.auth.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.contants.SettingCode;
import com.naas.admin_service.core.utils.JwtLite;
import com.naas.admin_service.core.utils.Utils;
import com.naas.admin_service.features.auth.dto.KeycloakLoginResposeDto;
import com.naas.admin_service.features.auth.dto.LoginDto;
import com.naas.admin_service.features.auth.dto.SecretDto;
import com.naas.admin_service.features.auth.dto.VerifyCaptchaDto;
import com.naas.admin_service.features.auth.service.AuthService;
import com.naas.admin_service.features.auth.service.CaptchaService;
import com.naas.admin_service.features.permission.cache.PermissionCacheWriter;
import com.naas.admin_service.features.permission.service.ComCfgPermissionService;
import com.naas.admin_service.features.setting.model.ComCfgSetting;
import com.naas.admin_service.features.setting.repository.ComCfgSettingRepository;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMappingRepository;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;
    private final CaptchaService captchaService;
    private final ComCfgSettingRepository settingRepository;
    private final Keycloak keycloak;
    private final ObjectMapper objectMapper;

    private final ComCfgPermissionService comCfgPermissionService;
    private final PermissionCacheWriter permissionCacheWriter;

    private final ObjectProvider<MultitenancyProperties> multitenancyPropsProvider;

    @Value("${multitenancy.enabled:false}")
    private boolean multitenancyEnabledFlag;

    @Value("${security.keycloak.base-url}")
    private String baseUrl;

    @Value("${security.keycloak.client-id}")
    private String clientId;

    @Value("${security.keycloak.client-secret}")
    private String clientSecret;

    @Value("${security.keycloak.redirect-uri}")
    private String redirectUri;

    @Value("${security.keycloak.realm}")
    private String realm;

    @Value("${security.keycloak.auth-uri}")
    private String keycloakBaseUrl;

    @Value("${security.keycloak.scope-openid-email-profile}")
    private String scope;

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    private static final TypeReference<Map<String, Object>> MAP_REF = new TypeReference<>() {};

    // =========================================================
    // Login entry
    // =========================================================
    @Override
    public Object login(LoginDto loginDto) {
        // 1) captcha
        VerifyCaptchaDto verifyCaptchaDto = loginDto.getVerifyCaptcha();
        ComCfgSetting setting = settingRepository.findBySettingCode(SettingCode.CAPTCHA.IS_APPLY).orElse(null);

        if (setting != null
                && "True".equals(setting.getSettingValue())
                && verifyCaptchaDto != null
                && !captchaService.verifyCaptcha(verifyCaptchaDto.getCode(), verifyCaptchaDto.getToken())) {
            throw new BusinessException(CommonErrorCode.CAPTCHA_INVALID);
        }

        // 2) user login
        if (loginDto.getTypeLogin() == null) {
            String effective = effectiveUsername(loginDto.getUsername());
            loginDto.setUsername(effective);

            checkUserBruteForceStatus(effective);

            Object res = loginUsingUser(loginDto);

            // ✅ cache permissions vào Redis (theo sub + group)
            cachePermissionsIfPossible(res);

            return res;
        }

        // 3) service-to-service (client credentials)
        return loginUsingClientCredentials(loginDto);
    }

    /**
     * ✅ Cache theo userId = sub (bạn muốn quản lý theo userId)
     * Redis keys:
     *  - perm:user:{sub}
     *  - perm:group:{groupName}
     */
    private void cachePermissionsIfPossible(Object res) {
        try {
            JsonNode node = objectMapper.valueToTree(res);
            String accessToken = node != null && node.hasNonNull("access_token")
                    ? node.get("access_token").asText()
                    : null;

            if (accessToken == null || accessToken.isBlank()) return;

            // userId = sub
            String userId = JwtLite.getSub(accessToken);
            if (userId == null || userId.isBlank()) return;

            Duration ttl = ttlFromResponse(node, accessToken);

            // Decode JWT payload để lấy groups (claim "groups")
            List<String> groups = extractGroupsFromJwt(accessToken);

            // 1) cache permissions theo USER_ID=sub
            Set<String> userPerms = comCfgPermissionService.findPermissionCodesByUserId(userId);
            permissionCacheWriter.putUser(userId, userPerms, ttl);

            // 2) cache permissions theo GROUP_NAME (nếu có groups)
            if (!groups.isEmpty()) {
                for (String g : groups) {
                    if (g == null || g.isBlank()) continue;
                    Set<String> gp = comCfgPermissionService.findPermissionCodesByGroupName(g);
                    permissionCacheWriter.putGroup(g, gp, ttl);
                }
            }
        } catch (Exception ignore) {
            // Không để cache làm fail login
        }
    }

    /**
     * Extract "groups" claim từ JWT payload (không cần verify signature, chỉ để cache).
     * JWT: header.payload.signature (Base64URL)
     */
    private List<String> extractGroupsFromJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return Collections.emptyList();

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> payload = objectMapper.readValue(payloadJson, MAP_REF);

            Object groupsObj = payload.get("groups");
            if (groupsObj instanceof Collection<?> c) {
                List<String> out = new ArrayList<>();
                for (Object o : c) {
                    if (o != null) out.add(String.valueOf(o));
                }
                return out;
            }
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static Duration ttlFromResponse(JsonNode node, String accessToken) {
        // 1) expires_in
        if (node != null && node.hasNonNull("expires_in")) {
            long seconds = node.get("expires_in").asLong();
            if (seconds > 5) return Duration.ofSeconds(seconds);
        }
        // 2) fallback exp - now
        Instant exp = JwtLite.getExp(accessToken);
        if (exp != null) {
            long seconds = exp.getEpochSecond() - Instant.now().getEpochSecond();
            if (seconds > 5) return Duration.ofSeconds(seconds);
        }
        // 3) default
        return Duration.ofMinutes(15);
    }

    // =========================================================
    // Brute force / User lookup
    // =========================================================
    public void checkUserBruteForceStatus(String username) {
        String userId = this.getUserIdByUsername(username);
        if (userId == null || userId.isBlank()) return;

        Map<String, Object> bruteForceStatus = keycloak.realm(realm)
                .attackDetection()
                .bruteForceUserStatus(userId);

        if (bruteForceStatus != null && Boolean.TRUE.equals(bruteForceStatus.get("disabled"))) {
            Object notBefore = bruteForceStatus.get("failedLoginNotBefore");
            String failedLoginNotBefore = notBefore == null ? "" : Utils.convertUnixTimestamp(notBefore.toString());
            throw new BusinessException(ErrorCode.LOCKED, ".Vui lòng đợi tới " + failedLoginNotBefore + " để tiếp tục!");
        }
    }

    public String getUserIdByUsername(String username) {
        if (username == null || username.isBlank()) return null;

        String u = username.trim();
        List<UserRepresentation> users = keycloak.realm(realm).users().search(u);
        return users.stream()
                .filter(x -> x.getUsername() != null && x.getUsername().equalsIgnoreCase(u))
                .findFirst()
                .map(UserRepresentation::getId)
                .orElse(null);
    }

    // =========================================================
    // Token endpoints
    // =========================================================
    @Override
    public Object exchangeCode(String code) {
        String authUrl = baseUrl + Constant.REALMS + realm + Constant.OPENID_CONNECT_TOKEN;

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(Constant.GRANT_TYPE, "authorization_code");
        body.add("code", code);
        body.add(Constant.CLIENT_ID, clientId);
        body.add(Constant.CLIENT_SECRET, clientSecret);
        body.add("redirect_uri", redirectUri);

        ResponseEntity<Map<String, Object>> response = postFormForMap(authUrl, body);

        // ✅ nếu bạn muốn cache luôn cho flow SSO exchangeCode
        Object res = response.getBody();
        cachePermissionsIfPossible(res);

        return res;
    }

    public Object loginUsingUser(LoginDto loginDto) {
        try {
            String authUrl = baseUrl + Constant.REALMS + realm + Constant.OPENID_CONNECT_TOKEN;

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add(Constant.GRANT_TYPE, "password");
            form.add(Constant.CLIENT_ID, clientId);
            form.add(Constant.CLIENT_SECRET, clientSecret);
            form.add("username", loginDto.getUsername());
            form.add("password", loginDto.getPassword());
            form.add(Constant.SCOPE, scope);

            ResponseEntity<Map<String, Object>> response = postFormForMap(authUrl, form);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
            throw new BusinessException(CommonErrorCode.USERNAME_PASSWORD_INCORECT);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BusinessException(CommonErrorCode.USERNAME_PASSWORD_INCORECT);
            }
            throw e;
        }
    }

    public Object loginUsingClientCredentials(LoginDto loginDto) {
        String authUrl = baseUrl + Constant.REALMS + realm + Constant.OPENID_CONNECT_TOKEN;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(Constant.GRANT_TYPE, "client_credentials");
        form.add(Constant.CLIENT_ID, loginDto.getUsername());
        form.add(Constant.CLIENT_SECRET, loginDto.getPassword());
        form.add(Constant.SCOPE, scope);

        ResponseEntity<Map<String, Object>> response = postFormForMap(authUrl, form);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new BusinessException(CommonErrorCode.INVALID_CREDENTIAL);
    }

    @Override
    public Object loginScret(SecretDto secretDto) {
        String authUrl = baseUrl + Constant.REALMS + realm + Constant.OPENID_CONNECT_TOKEN;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(Constant.GRANT_TYPE, "client_credentials");
        form.add(Constant.CLIENT_ID, secretDto.getClientId());
        form.add(Constant.CLIENT_SECRET, secretDto.getClientSecret());
        form.add(Constant.SCOPE, scope);

        ResponseEntity<Map<String, Object>> response = postFormForMap(authUrl, form);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new BusinessException(CommonErrorCode.INVALID_CREDENTIAL);
    }

    public Object refreshToken(String refreshToken) {
        String authUrl = baseUrl + Constant.REALMS + realm + Constant.OPENID_CONNECT_TOKEN;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(Constant.GRANT_TYPE, "refresh_token");
        form.add(Constant.CLIENT_ID, clientId);
        form.add("refresh_token", refreshToken);
        form.add(Constant.SCOPE, scope);

        ResponseEntity<Map<String, Object>> response = postFormForMap(authUrl, form);

        if (response.getStatusCode() == HttpStatus.OK) {
            Object res = response.getBody();
            // ✅ refresh cũng có access_token mới, cache lại luôn để chắc chắn TTL đúng
            cachePermissionsIfPossible(res);
            return res;
        }
        throw new BusinessException(CommonErrorCode.REFRESH_TOKEN_INVALID);
    }

    // =========================================================
    // Profile
    // =========================================================
    @Override
    public Object getProfileUser() {
        String token = ((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getToken()
                .getTokenValue();

        String url = baseUrl + Constant.REALMS + realm + "/protocol/openid-connect/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                MAP_TYPE
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new BusinessException(CommonErrorCode.USERNAME_PASSWORD_INCORECT);
    }

    // =========================================================
    // Resources by username
    // =========================================================
    @Override
    public List<ListResourceMappingDto> getListByUsername(String username) {
        String effective = effectiveUsername(username);
        String userId = this.getUserIdByUsername(effective);
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, username);
        }
        return this.ctgCfgResourceMappingRepository.findAllListResourceMappingDto(userId);
    }

    // =========================================================
    // Login URL (SSO)
    // =========================================================
    @Override
    public KeycloakLoginResposeDto getLoginUrl() {
        String stateParam = "";
        if (isMultitenancyEnabled()) {
            String tenant = currentTenantIdRequired();
            String nonce = UUID.randomUUID().toString();
            String state = "nonce=" + nonce + "&tenant=" + tenant;
            stateParam = "&state=" + URLEncoder.encode(state, StandardCharsets.UTF_8);
        }

        String url = "%s?client_id=%s&response_type=%s&scope=%s&redirect_uri=%s%s".formatted(
                keycloakBaseUrl,
                clientId,
                Constant.RESPONSE_TYPE,
                scope,
                redirectUri,
                stateParam
        );

        return new KeycloakLoginResposeDto(url);
    }

    // =========================================================
    // Multitenancy helpers
    // =========================================================
    private boolean isMultitenancyEnabled() {
        MultitenancyProperties p = multitenancyPropsProvider.getIfAvailable();
        if (p != null) return p.isEnabled();
        return multitenancyEnabledFlag;
    }

    private String currentTenantIdRequired() {
        if (!isMultitenancyEnabled()) return null;

        String t = TenantContext.getTenantId();
        if (t == null || t.isBlank()) {
            throw new BusinessException(CommonErrorCode.KEYCLOAK_UNKNOWN_ERROR);
        }
        return t.trim();
    }

    private String effectiveUsername(String rawUsername) {
        if (rawUsername == null) return null;

        String u = rawUsername.trim();
        if (u.isEmpty()) return u;

        if (!isMultitenancyEnabled()) return u;

        String tenantId = currentTenantIdRequired();
        String suffix = "." + tenantId;

        if (u.toLowerCase().endsWith(suffix.toLowerCase())) return u;
        return u + suffix;
    }

    // =========================================================
    // RestTemplate helper
    // =========================================================
    private ResponseEntity<Map<String, Object>> postFormForMap(String url, MultiValueMap<String, String> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                MAP_TYPE
        );
    }
}
