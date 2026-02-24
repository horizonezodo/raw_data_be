package com.ngv.zns_service.dto.response.imageStorage;

import lombok.Data;

@Data
public class ImageStorageDtoConvertJson {
    private String id;
    private String url;
    private String name;
    private String phone;
    private String dateTime;
    private String address;
    private String price;
    private Integer durationTime;
    private byte[] image;

    public ImageStorageDtoConvertJson() {
    }

    public ImageStorageDtoConvertJson(String id, String url, String name, String phone, String dateTime, String address,
                                      String price, byte[] image, Integer durationTime) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.phone = phone;
        this.dateTime = dateTime;
        this.address = address;
        this.price = price;
        this.image = image;
        this.durationTime = durationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }
}
