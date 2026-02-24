package ngvgroup.com.bpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages =
        {
                "com.ngvgroup.bpm.core.persistence" ,
                "ngvgroup.com.bpm"
        })
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "ngvgroup.com.bpm")
public class BpmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BpmApplication.class, args);
    }

}
