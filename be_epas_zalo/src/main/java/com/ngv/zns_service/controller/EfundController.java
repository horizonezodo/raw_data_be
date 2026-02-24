package com.ngv.zns_service.controller;

import com.ngv.zns_service.dto.request.ZaloMiniApp.AIADataInqRequest;
import com.ngv.zns_service.dto.request.ZaloMiniApp.ZMADataInqRequest;
import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngv.zns_service.dto.response.ZaloMiniApp.AIADataInqResponse;
import com.ngv.zns_service.dto.response.ZaloMiniApp.ZMADataInqResponse;
import com.ngv.zns_service.service.EfundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/efund")
@RequiredArgsConstructor
public class EfundController {

    private final EfundService efundService;

    @GetMapping("/test")
    public ResponseEntity<ResponseData<Object>> testCall(@RequestParam String partnerCode) {
        String res = efundService.callEfundApi(partnerCode);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-login")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaLogin(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request) {
        ZMADataInqResponse res = efundService.EFfundZMALogin(partnerCode, request);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-public-info")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaPublicInfo(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request) {
        ZMADataInqResponse res = efundService.EFfundZMAPublicInfo(partnerCode, request);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-signup")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaSignup(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request) {
        ZMADataInqResponse res = efundService.EFfundZMASignup(partnerCode, request);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-data-inq")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaDataInq(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request, @RequestHeader(value = "X-USERZMP-TOKEN", required = false) String zmpToken) {
        ZMADataInqResponse res = efundService.EFfundZMADataInq(partnerCode, request, zmpToken);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-create-tran-core")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaCreateTranCore(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request, @RequestHeader(value = "X-USERZMP-TOKEN", required = false) String zmpToken) {
        ZMADataInqResponse res = efundService.EFfundZMACreateTranCore(partnerCode, request, zmpToken);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/zma-transaction-registration")
    public ResponseEntity<ResponseData<ZMADataInqResponse>> zmaTransactionRegistration(@RequestParam String partnerCode, @RequestBody ZMADataInqRequest request, @RequestHeader(value = "X-USERZMP-TOKEN", required = false) String zmpToken) {
        ZMADataInqResponse res = efundService.EFfundZMATransactionRegistration(partnerCode, request, zmpToken);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @PostMapping("/aia-data-inq")
    public ResponseEntity<ResponseData<AIADataInqResponse>> aiaDataInq(@RequestParam String partnerCode, @RequestBody AIADataInqRequest request, @RequestHeader(value = "X-USERZMP-TOKEN", required = false) String zmpToken) {
        AIADataInqResponse res = efundService.EFfundAIADataInq(partnerCode, request, zmpToken);
        return ResponseData.okEntity(res);
    }

    @PreAuthorize("hasRole(T(com.ngv.zns_service.constant.Roles).CLIENT_ZMA)")
    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(
            @RequestParam String partnerCode, 
            @RequestParam String imageName
            ) {
        
        try {
            // Stream image từ external source thông qua service
            byte[] imageData = efundService.getImageStream(partnerCode, imageName);
            
            // Determine content type based on file extension
            String contentType = getContentType(imageName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageData);
                    
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String getContentType(String imageName) {
        String extension = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            default:
                return "image/jpeg";
        }
    }
}
