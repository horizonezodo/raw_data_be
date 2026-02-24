package vn.com.amc.qtdl.tableau_proxy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import vn.com.amc.qtdl.tableau_proxy.configs.WhitelistProperties;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(WhitelistProperties.class)
public class TableauProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableauProxyApplication.class, args);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
	}

}
