package vn.com.amc.qtdl.bi_proxy.enums;

import lombok.Getter;

@Getter
public enum RecordStatus {

    WAIT("wait", "Nhập chờ duyệt"),
    SUBMIT("submit", "Trình duyệt"),
    REJECT("reject", "Từ chối duyệt"),
    CANCEL("cancel", "Hủy"),
    APPROVAL("approval", "Đã duyệt");

    private final String code;
    private final String label;

    RecordStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static RecordStatus fromCode(String code) {
        for (RecordStatus status : RecordStatus.values()) {
            if (status.code.equalsIgnoreCase(code)) {
                return status;
            }
        }
        return APPROVAL;
    }

}
