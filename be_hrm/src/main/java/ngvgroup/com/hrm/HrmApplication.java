package ngvgroup.com.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages =
        {
                "com.ngvgroup.bpm.core.persistence" ,
                "ngvgroup.com.hrm",
                "ngvgroup.com.bpm"
        })
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "ngvgroup.com.hrm")
public class HrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(HrmApplication.class, args);
    }

}
