package com.naas.admin_service.core.contants;

public class EntityStatus {

    public enum IsDelete {
        DELETED(1), // Đã xóa
        NOT_DELETED(0); // Chưa xóa

        private final int value;

        IsDelete(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum IsActive {
        ACTIVE(1), // Hiệu lực
        NOT_ACTIVE(0); // Hết hiệu lực

        private final int value;

        IsActive(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum RecordStatus {
        APPROVAL("approval"), // Đã duyệt
        REJECT("reject"), // Từ chối duyệt
        WAIT("wait"), // Nhập chờ duyệt
        CANCEL("cancel"), // Hủy
        SUBMIT("submit"); // Trình duyệt

        private final String value;

        RecordStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
