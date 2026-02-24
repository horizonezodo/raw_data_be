package com.ngv.zns_service.dto.response.imageStorage;

import com.ngv.zns_service.util.converter.HashMapConverter;
import jakarta.persistence.Convert;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ImageStorageDto {
    private String id;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> properties = new HashMap<>();
    private Integer durationTime;

    private byte[] imageData;


    public ImageStorageDto() {
    }

    public ImageStorageDto(String id, Map<String, Object> properties, byte[] imageData, Integer durationTime) {
        this.id = id;
        this.properties = properties;
        this.imageData = imageData;
        this.durationTime = durationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }
}
