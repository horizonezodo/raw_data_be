package com.naas.admin_service.features.me.controller;

import com.naas.admin_service.features.me.dto.ChangeMyPasswordRequest;
import com.naas.admin_service.features.me.dto.MeProfileDto;
import com.naas.admin_service.features.me.dto.UpdateMeRequest;
import com.naas.admin_service.features.me.service.MeService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/me")
public class MeController {

    private final MeService meService;

    @GetMapping
    public ResponseEntity<ResponseData<MeProfileDto>> me() {
        return ResponseData.okEntity(meService.me());
    }

    @PutMapping
    public ResponseEntity<ResponseData<Void>> update(@Valid @RequestBody UpdateMeRequest req) {
        meService.updateProfile(req);
        return ResponseData.createdEntity();
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseData<Void>> changePassword(@Valid @RequestBody ChangeMyPasswordRequest req) {
        meService.changePassword(req);
        return ResponseData.createdEntity();
    }

    @PostMapping("/avatar")
    public ResponseEntity<ResponseData<String>> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return ResponseData.okEntity(meService.uploadAvatar(file));
    }
}
