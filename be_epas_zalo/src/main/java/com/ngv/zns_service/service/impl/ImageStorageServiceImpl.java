package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngv.zns_service.config.ZnsConfig;
import com.ngv.zns_service.dto.response.imageStorage.ImageStorageDto;
import com.ngv.zns_service.dto.response.imageStorage.ImageStorageDtoConvertJson;
import com.ngv.zns_service.model.entity.ImageStorage;
import com.ngv.zns_service.repository.ImageStorageRepository;
import com.ngv.zns_service.service.ImageStorageService;
import com.ngv.zns_service.util.converter.HashMapConverter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class ImageStorageServiceImpl implements ImageStorageService {
    private static final Logger log = LogManager.getLogger(ImageStorageServiceImpl.class);
    private final ImageStorageRepository imageStorageRepository;
    private final ZnsConfig znsConfig;
    private final HashMapConverter converter;

    public ImageStorageServiceImpl(ImageStorageRepository imageStorageRepository, ZnsConfig znsConfig, HashMapConverter converter) {
        this.imageStorageRepository = imageStorageRepository;
        this.znsConfig = znsConfig;
        this.converter = converter;
    }

    @Override
    public ImageStorageDto create(Map<String, Object> objectMap, byte[] file) {

        ImageStorage imageStorage = new ImageStorage();
        imageStorage.setId(UUID.randomUUID().toString());
        objectMap.put("url", znsConfig.getUrl() + "?id=" + imageStorage.getId());
        imageStorage.setProperties(objectMap);
        imageStorage.setImageData(file);
        imageStorage.setDurationTime(600);

        imageStorageRepository.save(imageStorage);

        ImageStorageDto dto = new ImageStorageDto();
        BeanUtils.copyProperties(imageStorage, dto);

        return dto;
    }

    @Override
    public ImageStorageDtoConvertJson getDetail(String id) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ImageStorage imageStorage = imageStorageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Not found for id: " + id));
            String map = converter.convertToDatabaseColumn(imageStorage.getProperties());
            ImageStorageDtoConvertJson json = objectMapper.readValue(map, ImageStorageDtoConvertJson.class);
            json.setId(imageStorage.getId());
            json.setImage(imageStorage.getImageData());
            json.setDurationTime(imageStorage.getDurationTime());
            return json;
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
            return null;
        }
    }
}
