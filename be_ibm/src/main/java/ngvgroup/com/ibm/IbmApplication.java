package ngvgroup.com.ibm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages =
        {
                "com.ngvgroup.bpm.core.persistence" ,
                "ngvgroup.com.ibm"
        })
@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "ngvgroup.com.ibm")
public class IbmApplication {

    public static void main(String[] args) {
        SpringApplication.run(IbmApplication.class, args);
    }

}
