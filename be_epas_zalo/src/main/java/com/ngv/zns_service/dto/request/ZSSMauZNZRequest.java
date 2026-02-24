package com.ngv.zns_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZSSMauZNZRequest {
    @NotBlank(message = "maDvi không được để trống")
    private String maDvi;

    @NotBlank(message = "zoaId không được để trống")
    private String zoaId;

    @NotBlank(message = "maMau không được để trống")
    private String maMau;

    @Size(min = 10, max = 60, message = "tenMau phải có ít nhất {min} ký tự và nhiều nhất {max} ký tự")
    private String tenMau;

    private String loaiZns;

    private String maLoaiMau;

    private BigDecimal donGia;

    private String loaiHanh;

    private byte[] logoSang;

    private byte[] logoToi;

    private byte[] anh1;

    private byte[] anh2;

    private byte[] anh3;

    private List<MultipartFile> images;

    private Map<String, MultipartFile> logos;

    @Size(min = 9, max = 65, message = "tdeMau phải có ít nhất {min} ký tự và nhiều nhất {max} ký tự")
    private String tdeMau;

    @Size(min = 9, max = 400, message = "ndungTde phải có ít nhất {min} ký tự và nhiều nhất {max} ký tự")
    private String ndungTde;

    private List<MauBangRequest> mauBangs;

    private String vban1;

    private String vban2;

    private String vban3;

    private String vban4;

    private List<MauButtonRequest> mauButtons;

    private List<CaiDatTSoRequest> tSoRequests;

    private String ghiChu;


}
