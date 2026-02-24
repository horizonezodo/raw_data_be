package com.ngv.zns_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages =
        {
                "com.ngvgroup.bpm.core.persistence" ,
                "com.ngv.zns_service"
        })
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.ngv.zns_service")
public class ZnsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZnsServiceApplication.class, args);
	}

}
