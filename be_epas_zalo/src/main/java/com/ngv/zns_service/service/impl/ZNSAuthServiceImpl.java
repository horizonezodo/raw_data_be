package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import com.ngv.zns_service.repository.ZSSAppRepository;
import com.ngv.zns_service.service.ZNSAuthService;
import com.ngv.zns_service.util.json.JacksonUtil;
import com.ngv.zns_service.util.log.DebugLogger;
import com.ngv.zns_service.util.string.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ZNSAuthServiceImpl implements ZNSAuthService {
    
    @Value("${zns.auth_uri}")
    private String authUri;
    
    @Value("${zns.redirect_uri}")
    private String redirectUri;
    
    @Value("${zns.token_uri}")
    private String tokenUri;
    
    private final Map<String, Object> verifiers = new HashMap<String, Object>();
    
    private final WebClient webClient;
    private final ZSSAppRepository zssAppRepository;
    
    @Override
    public String requestLinkAuthorize(ObjectNode payload) {
        /*init code verifier*/
        String state = StringUtil.generateUUID();
        String codeVerifier = genCodeVerifier();
        payload.put("codeVerifier", codeVerifier);
        verifiers.put(state, payload);

        /*Get app*/
        String maDvi = payload.get("maDvi").asText();
        String appId;
        Optional<ZssApp> zssApp = zssAppRepository.findByMaDvi(maDvi);
        if (zssApp.isPresent()) {
            appId = zssApp.get().getAppId();
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND, "No app found with maDvi: " + maDvi);
        }

        StringBuilder path = new StringBuilder();
        try {
            path.append(authUri);
            path.append("?");
            path.append("app_id=").append(appId);
            path.append("&");
            path.append("redirect_uri=").append(redirectUri);
            path.append("&");
            path.append("code_challenge=").append(genCodeChallenge(codeVerifier));
            path.append("&");
            path.append("state=").append(state);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
        }
        return path.toString();
    }
    
    @Override
    public ZssTKhoanZoa requestAccessToken(String oaId, String authorizationCode, String state) {
        ObjectNode verifierInfo = JacksonUtil.toJsonObject(verifiers.get(state).toString());
        String maDvi = verifierInfo.get("maDvi").asText();
        ZssApp zssApp = zssAppRepository.findByMaDvi(maDvi).orElse(null);
        ObjectNode tokenResponse = JacksonUtil.toJsonObject(invokeGetToken(authorizationCode, state, zssApp));
        if (tokenResponse != null && tokenResponse.has("error")) {
            if (tokenResponse.get("error").asInt() != 0)
                throw new IllegalStateException(tokenResponse.get("error_name").asText());
        }

        ZssTKhoanZoa zssTKhoanZoa = new ZssTKhoanZoa();
        zssTKhoanZoa.setMaZoa(oaId);
        zssTKhoanZoa.setAccessToken(tokenResponse.get("access_token").asText());
        zssTKhoanZoa.setRefreshToken(tokenResponse.get("refresh_token").asText());
        zssTKhoanZoa.setNgayDhanTk(tokenResponse.get("expires_in").asLong() * 1000 + System.currentTimeMillis());
        zssTKhoanZoa.setMaDvi(maDvi);
        zssTKhoanZoa.setAppId(zssApp.getAppId());
        return zssTKhoanZoa;
    }
    
    @Override
    public ZssTKhoanZoa refreshAccessToken(String oaId, String refreshToken, ZssApp zssApp) {
        ObjectNode tokenResponse = JacksonUtil.toJsonObject(invokeRefreshToken(refreshToken, zssApp));
        if (tokenResponse != null && tokenResponse.has("error")) {
            if (tokenResponse.get("error").asInt() != 0)
                throw new IllegalStateException(tokenResponse.get("error_name").asText());
        }
        ZssTKhoanZoa zssTKhoanZoa = new ZssTKhoanZoa();
        zssTKhoanZoa.setMaZoa(oaId);
        zssTKhoanZoa.setAccessToken(tokenResponse.get("access_token").asText());
        zssTKhoanZoa.setRefreshToken(tokenResponse.get("refresh_token").asText());
        zssTKhoanZoa.setNgayDhanTk(tokenResponse.get("expires_in").asLong() * 1000 + System.currentTimeMillis());
        return zssTKhoanZoa;
    }
    
    private String invokeGetToken(String authorizationCode, String state, ZssApp zssApp) {
        ObjectNode verifierInfo = JacksonUtil.toJsonObject(verifiers.get(state).toString());
        String codeVerifier = verifierInfo.get("codeVerifier").asText();
        verifierInfo.remove(state);
        return webClient.post()
            .uri(tokenUri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("secret_key", zssApp.getAppSecret())
            .bodyValue(
                "code=" + authorizationCode +
                "&app_id=" + zssApp.getAppId() +
                "&grant_type=authorization_code" +
                "&code_verifier=" + codeVerifier
            )
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    @Async("taskExecutor")
    public String invokeRefreshToken(String refreshToken, ZssApp zssApp) {
        return webClient.post()
            .uri(tokenUri)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("secret_key", zssApp.getAppSecret())
            .bodyValue(
                "refresh_token=" + refreshToken +
                "&app_id=" + zssApp.getAppId() +
                "&grant_type=refresh_token"
            )
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    
    public String genCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String code = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return code;
    }
    
    public String genCodeChallenge(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
