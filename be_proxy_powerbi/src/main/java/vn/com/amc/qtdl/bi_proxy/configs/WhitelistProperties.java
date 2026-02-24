package vn.com.amc.qtdl.bi_proxy.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.whitelist")
public class WhitelistProperties {

    private List<String> paths;
    private String pattern;

}
