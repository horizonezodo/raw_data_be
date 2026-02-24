package com.naas.admin_service.features.setting.controller;

import com.naas.admin_service.features.setting.dto.RemotesUrlDto;
import com.naas.admin_service.features.setting.model.RemotesUrl;
import com.naas.admin_service.features.setting.service.RemoteUrlService;
import com.ngvgroup.bpm.core.common.dto.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/remotes")
@RequiredArgsConstructor
public class RemoteEntryController {
    private final RemoteUrlService remoteUrlService;

    @GetMapping("/url")
    public ResponseEntity<ResponseData<Map<String, String>>> listRemoteUrl() {
        Map<String, String> remotesUrl = remoteUrlService.getListRemoteUrl();
        return ResponseData.okEntity(remotesUrl);
    }

    @GetMapping("/module")
    public ResponseEntity<List<RemotesUrl>> listRemoteModule() {
        List<RemotesUrl> remotesModules = remoteUrlService.getListRemoteModule();
        return ResponseEntity.status(HttpStatus.OK).body(remotesModules);
    }


    @PreAuthorize("hasRole('admin_remote')")
    @PostMapping
    public ResponseEntity<ResponseData<Void>> createRemoteUrl(@Valid @RequestBody RemotesUrlDto remotesUrlDto){
        remoteUrlService.createRemoteUrl(remotesUrlDto);
        return ResponseData.createdEntity();
    }

    @PreAuthorize("hasRole('admin_remote')")
    @PutMapping("/{remoteName}")
    public ResponseEntity<ResponseData<Void>> updateRemoteUrl(@PathVariable String remoteName, @Valid @RequestBody RemotesUrlDto remotesUrlDto){
        remoteUrlService.updateRemoteUrl(remoteName, remotesUrlDto);
        return ResponseData.okEntity(null);
    }

    @PreAuthorize("hasRole('admin_remote')")
    @DeleteMapping("/{remoteName}")
    public ResponseEntity<ResponseData<Void>> deleteRemoteUrl(@PathVariable String remoteName){
        remoteUrlService.deleteRemoteUrl(remoteName);
        return ResponseData.noContentEntity();
    }

}
