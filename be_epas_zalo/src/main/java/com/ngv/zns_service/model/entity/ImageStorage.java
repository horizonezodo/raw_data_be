package com.ngv.zns_service.model.entity;

import com.ngv.zns_service.util.converter.HashMapConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class ImageStorage {

    @Id
    private String id;

    @Convert(converter = HashMapConverter.class)
    @Column(name = "properties", columnDefinition = "NVARCHAR2(255)")
    private Map<String, Object> properties = new HashMap<>();

    @Column(name = "duration_time", columnDefinition = "NUMBER")
    private Integer durationTime;

    @Column(name = "expire_time", columnDefinition = "DATE")
    private LocalDateTime expireTime;

    @Lob
    private byte[] imageData;

    public ImageStorage() {
    }

    public ImageStorage(String id, Map<String, Object> properties, byte[] imageData, Integer durationTime) {
        this.id = id;
        this.properties = properties;
        this.imageData = imageData;
        this.durationTime = durationTime;
        this.expireTime = LocalDateTime.now().plusSeconds(durationTime);
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
        this.expireTime = LocalDateTime.now().plusSeconds(durationTime);
    }
}
