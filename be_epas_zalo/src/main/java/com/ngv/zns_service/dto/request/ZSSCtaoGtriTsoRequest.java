package com.ngv.zns_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ZSSCtaoGtriTsoRequest {
    @NotBlank(message = "Mã cấu tạo không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Mã cấu tạo chỉ chứa chữ, số, dấu chấm (.), gạch dưới (_), gạch ngang (-)")
    @Pattern(regexp = "^[^.].*$", message = "Mã cấu tạo không được bắt đầu bằng dấu chấm (.)")
    @Pattern(regexp = "^\\S.*$", message = "Mã cấu tạo không được bắt đầu bằng khoảng trắng")
    private String maCtaoGtriTso;

    @NotBlank(message = "Tên cấu tạo không được để trống")
    @Pattern(regexp = "^\\S.*$", message = "Tên cấu tạo không được bắt đầu bằng khoảng trắng")
    private String tenCtaoGtriTso;

    @NotBlank(message = "Công thức cấu tạo không được để trống")
    @Pattern(regexp = "^\\S.*$", message = "Công thức cấu tạo không được bắt đầu bằng khoảng trắng")
    private String cthucCtaoGtriTso;

    private String loaiGiaTri;
    private String mdichSdung;
}
