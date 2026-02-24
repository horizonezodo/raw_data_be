package com.naas.admin_service.core.contants;

public class SettingCode {
    private SettingCode(){}

    public static class LAYOUT {
        private LAYOUT(){}

        public static final String SYSTEM_NAME = "Layout_SystemName";
        public static final String PHONE = "Layout_Phone";
        public static final String MAIL = "Layout_Mail";
        public static final String WEBSITE = "Layout_Website";
        public static final String IMAGE_LOGIN = "Layout_ImageLogin";
        public static final String IMAGE_LEFT = "Layout_ImageLeft";
        public static final String SSO = "Layout_Login_SSO";
        public static final String SESSION = "Session";
        public static final String HEADER = "Layout_Header";
        public static final String FOOTER = "Layout_Footer";
        public static final String IS_FULL_FOOTER = "Layout_IsFullFooter";

    }

    public static class CAPTCHA {
        private CAPTCHA(){}

        public static final String LENGTH = "Captcha_Length";
        public static final String DATA_TYPE = "Captcha_DataType";
        public static final String CASE_SENSITIVE = "Captcha_CaseSensitive";
        public static final String IS_APPLY = "Captcha_IsApply";
    }

    public static class CaptchaDefault {
        private CaptchaDefault(){}

        public static final Number LENGTH = 4;
        public static final String DATA_TYPE = "Numberic";
        public static final String CASE_SENSITIVE = "Mix";
    }

    public static class CaptchaComparison {
        private CaptchaComparison(){}

        public static final String UPPERCASE = "Uppercase";
        public static final String LOWERCASE = "Lowercase";
        public static final String MIX = "Mix";
        public static final String NUMBERIC = "Numberic";
        public static final String ALPHA = "Alpha";
    }
}
