package ngvgroup.com.bpm.core.config;

import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Component
@Primary
@RequiredArgsConstructor
public class CamundaAuditorAware implements AuditorAware<String> {

    private final IdentityService identityService; // Của Camunda

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        // 1. Ưu tiên: Lấy User đang được set trong context của Camunda
        Authentication camundaAuth = identityService.getCurrentAuthentication();
        if (camundaAuth != null && camundaAuth.getUserId() != null) {
            return Optional.of(camundaAuth.getUserId());
        }

        // 2. Fallback: Lấy từ Spring Security (Token client gửi lên)
        return Optional.of("system");
    }
}