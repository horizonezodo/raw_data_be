package com.ngv.zns_service.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.model.ResponseData;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import com.ngv.zns_service.service.ZNSAuthService;
import com.ngv.zns_service.service.ZSSAppService;
import com.ngv.zns_service.service.ZssTKhoanZoaService;
import com.ngv.zns_service.util.http.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class OauthController {

    @Value("${zns.oa_link_uri}")
    private String oaLinkUri;

    private final ZNSAuthService znsAuthService;
    private final ZssTKhoanZoaService zssTKhoanZoaService;
    private final ZSSAppService zSSAppService;

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).USER_MODIFY)")
    @PostMapping("/auth/link")
    public ResponseEntity<ResponseData<String>> requestLinkAuth(@RequestBody ObjectNode payload) {
        String linkAuth = znsAuthService.requestLinkAuthorize(payload);
        return ResponseUtils.success(linkAuth);
    }

    @GetMapping("/auth/callback")
    public void requestTokenAuth(@RequestParam String oa_id, @RequestParam String code,
                                                                 @RequestParam String state,
                                                                 HttpServletResponse response) throws IOException {
        ZssTKhoanZoa zoa = zssTKhoanZoaService.setToken(oa_id, code, state);
        String encodedTenZOA = URLEncoder.encode(zoa.getTenZoa(), StandardCharsets.UTF_8);
        String linkCallback;
        if (zoa != null) {
            linkCallback = oaLinkUri + "?result=success&tenZOA=" + encodedTenZOA;
        } else {
            linkCallback = oaLinkUri + "?result=failed";
        }
        response.sendRedirect(linkCallback);
    }

    @GetMapping("/{filename:.+\\.html}")
    public ResponseEntity<String> getVerificationFile1(@PathVariable String filename) {
        ZssApp zssApp = zSSAppService.getByWebhookVerificationFile(filename);
        String htmlContent = zssApp.getWebhookVerificationContent();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlContent);
    }
}
