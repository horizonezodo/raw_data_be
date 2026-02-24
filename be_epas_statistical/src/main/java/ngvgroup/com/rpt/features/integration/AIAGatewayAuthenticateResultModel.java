package ngvgroup.com.rpt.features.integration;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AIAGatewayAuthenticateResultModel {

    private String accessToken;

    private String encryptedAccessToken;

    private int expireInSeconds;

    private boolean shouldResetPassword;

    private String passwordResetCode;

    private long userId;

    private boolean requiresTwoFactorVerification;

    private List<String> twoFactorAuthProviders;

    private String twoFactorRememberClientToken;

    private String returnUrl;

    private String refreshToken;

    private LocalDateTime expireAt;

    private String partnerCode;
}