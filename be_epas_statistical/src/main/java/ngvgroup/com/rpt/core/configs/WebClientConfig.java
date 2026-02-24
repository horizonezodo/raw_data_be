package ngvgroup.com.rpt.core.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // Tăng giới hạn buffer từ 256KB lên 10MB để xử lý response lớn từ Efund API
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> {
                    CodecConfigurer.DefaultCodecs defaults = codecs.defaultCodecs();
                    defaults.maxInMemorySize(10 * 1024 * 1024); // 10MB
                })
                .build();
                
        return builder
                .exchangeStrategies(strategies)
                .build();
    }
}
