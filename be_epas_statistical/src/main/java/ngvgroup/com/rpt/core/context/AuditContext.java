package ngvgroup.com.rpt.core.context;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class AuditContext {
    private final Timestamp now;
    private final String username;

    public AuditContext() {
        this.now = Timestamp.from(ZonedDateTime.now(ZoneId.systemDefault()).toInstant());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            this.username = jwt.getClaimAsString("preferred_username");
        } else if (auth != null) {
            this.username = auth.getName();
        } else {
            this.username = "system";
        }
    }
}
