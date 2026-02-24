package com.naas.admin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@EnableFeignClients(basePackages = {
        "com.naas.admin_service",
})
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.naas.admin_service",
        "com.ngvgroup.bpm.core.persistence"
})
public class AdminServiceApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SpringApplication.run(AdminServiceApplication.class, args);
    }
}
