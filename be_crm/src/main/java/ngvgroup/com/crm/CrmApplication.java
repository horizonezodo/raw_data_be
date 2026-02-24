package ngvgroup.com.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
        "com.ngvgroup.bpm.core.persistence",
        "ngvgroup.com.crm",
        "ngvgroup.com.bpm"
})
@SpringBootApplication
@EnableFeignClients(basePackages = {
        "ngvgroup.com.crm",
})
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}