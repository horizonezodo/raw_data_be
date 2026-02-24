package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.response.imageStorage.ImageStorageDto;
import com.ngv.zns_service.dto.response.imageStorage.ImageStorageDtoConvertJson;

import java.util.Map;

public interface ImageStorageService {
    ImageStorageDto create( Map<String, Object> objectMap, byte[] file);

    ImageStorageDtoConvertJson getDetail(String id);
}
