package ngvgroup.com.bpm.client.autoconfigure;

import ngvgroup.com.bpm.client.feign.BpmFeignClient;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot 3 auto-configuration entrypoint for bpm-client.
 *
 * <p>
 * Goal: consumers only need to add the dependency; no manual {@code @ComponentScan} required.
 * </p>
 */
@AutoConfiguration
@ComponentScan(basePackages = "ngvgroup.com.bpm.client")
@ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignClient")
@EnableFeignClients(basePackageClasses = BpmFeignClient.class)
public class BpmClientAutoConfiguration {
}

