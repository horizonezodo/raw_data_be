package vn.com.amc.qtdl.bi_proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BiProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiProxyApplication.class, args);
	}

}
