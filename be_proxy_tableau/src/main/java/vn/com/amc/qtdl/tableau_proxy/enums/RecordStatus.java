package vn.com.amc.qtdl.tableau_proxy.enums;

import lombok.Getter;

@Getter
public enum RecordStatus {

    NCD("Nhập chờ duyệt"),
    TD("Trình duyệt"),
    TCD("Từ chối duyệt"),
    DD("Đã duyệt");

    final String name;
    RecordStatus(String name) {
        this.name = name;
    }
}
