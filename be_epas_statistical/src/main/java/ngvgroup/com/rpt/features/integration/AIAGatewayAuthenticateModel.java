package ngvgroup.com.rpt.features.integration;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AIAGatewayAuthenticateModel {

    @NotNull
    @Size(max = 128)
    private String userNameOrEmailAddress;

    @NotNull
    @Size(max = 128)
    private String password;

    private String twoFactorVerificationCode;

    private boolean rememberClient;

    private String twoFactorRememberClientToken;

    private Boolean singleSignIn;

    private String returnUrl;
}