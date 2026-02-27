package com.naas.admin_service.features.me.service.impl;

import com.naas.admin_service.core.provider.IdentityStoreService;
import com.naas.admin_service.features.common.service.MinIOService;
import com.naas.admin_service.features.me.dto.ChangeMyPasswordRequest;
import com.naas.admin_service.features.me.dto.MeProfileDto;
import com.naas.admin_service.features.me.dto.UpdateMeRequest;
import com.naas.admin_service.features.me.service.MeService;
import com.naas.admin_service.features.me.service.SystemLoginModeService;
import com.naas.admin_service.features.common.tenant.TenantUsernameResolver;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import lombok.RequiredArgsConstructor;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;

@Service
@RequiredArgsConstructor
public class MeServiceImpl extends BaseStoredProcedureService implements MeService {

    private final SystemLoginModeService systemLoginModeService;
    private final MinIOService minIOService;
    private final IdentityStoreService identityStoreService;
    private final TenantUsernameResolver usernameResolver;

    @Value("${minio.public-url}")
    private String minioPublicUrl;

    @Override
    public MeProfileDto me() {
        String userId = getCurrentUserId();

        UserResource ur = identityStoreService.getUser(userId);
        UserRepresentation rep = ur.toRepresentation();

        MeProfileDto dto = new MeProfileDto();
        dto.setUsername(rep.getUsername());
        dto.setEmail(rep.getEmail());
        dto.setFirstName(rep.getFirstName());
        dto.setLastName(rep.getLastName());
        dto.setFullName(buildFullName(rep.getFirstName(), rep.getLastName()));
        dto.setSsoOnly(systemLoginModeService.isSsoOnly());
        dto.setAvatarUrl(minioPublicUrl + "/images/avatars/" + userId + "/avatar.png");
        return dto;
    }

    @Override
    public void updateProfile(UpdateMeRequest req) {
        blockIfSsoOnly();
        String userId = getCurrentUserId();

        UserResource ur = identityStoreService.getUser(userId);
        UserRepresentation rep = ur.toRepresentation();

        rep.setFirstName(req.getFirstName().trim());
        rep.setLastName(req.getLastName().trim());

        String newEmail = req.getEmail().trim();
        if (!newEmail.equals(rep.getEmail())) {
            rep.setEmail(newEmail);
            rep.setEmailVerified(false);
        }

        // ✅ Keycloak không cho đổi username theo yêu cầu
        ur.update(rep);
    }

    @Override
    public void changePassword(ChangeMyPasswordRequest req) {
        if (req == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Request không hợp lệ");
        }

        if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "New password không hợp lệ");
        }

        if (req.getConfirmPassword() != null && !Objects.equals(req.getNewPassword(), req.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Confirm password không khớp");
        }

        blockIfSsoOnly();

        String userId = getCurrentUserId();
        String username = getCurrentUserName();
        if (username == null || username.isBlank()) {
            username = identityStoreService.getUser(userId).toRepresentation().getUsername();
        }

        String tenantId = TenantContext.getTenantId();
        boolean enabled = tenantId != null && !tenantId.isBlank();
        String effective = usernameResolver.effectiveUsername(username, enabled, tenantId);

        verifyCurrentPassword(effective, req.getCurrentPassword());

        CredentialRepresentation cr = new CredentialRepresentation();
        cr.setType(CredentialRepresentation.PASSWORD);
        cr.setTemporary(false);
        cr.setValue(req.getNewPassword());
        identityStoreService.resetUserPassword(userId, cr);
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "File avatar không hợp lệ");
        }

        String userId = getCurrentUserId();

        // cố định 1 file -> ghi đè
        String objectName = "/images/avatars/" + userId + "/avatar.png";

        minIOService.uploadFileWithObjectName(objectName, file); // method bạn vừa thêm
        // url cố định
        return minioPublicUrl + objectName + "?v=" + System.currentTimeMillis();
    }

    // =========================================================
    // Helpers
    // =========================================================
    private void blockIfSsoOnly() {
        if (systemLoginModeService.isSsoOnly()) {
            throw new BusinessException(ErrorCode.FORBIDDEN,
                    "Hệ thống đang bật SSO, không cho phép chỉnh sửa thông tin/đổi mật khẩu");
        }
    }

    private String buildFullName(String first, String last) {
        String f = first == null ? "" : first.trim();
        String l = last == null ? "" : last.trim();
        String s = (f + " " + l).trim();
        return s.isBlank() ? null : s;
    }

    private void verifyCurrentPassword(String username, String currentPassword) {
        identityStoreService.verifyCurrentPassword(username, currentPassword);
    }
}
