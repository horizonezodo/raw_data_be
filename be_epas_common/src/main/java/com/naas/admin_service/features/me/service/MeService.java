package com.naas.admin_service.features.me.service;

import com.naas.admin_service.features.me.dto.ChangeMyPasswordRequest;
import com.naas.admin_service.features.me.dto.MeProfileDto;
import com.naas.admin_service.features.me.dto.UpdateMeRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MeService {
    MeProfileDto me();

    void updateProfile(UpdateMeRequest req);

    void changePassword(ChangeMyPasswordRequest req);

    String uploadAvatar(MultipartFile file);
}
