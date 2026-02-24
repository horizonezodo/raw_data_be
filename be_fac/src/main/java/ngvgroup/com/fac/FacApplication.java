package ngvgroup.com.fac;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages =
        {
                "com.ngvgroup.bpm.core.persistence" ,
                "ngvgroup.com.fac",
                "ngvgroup.com.bpm"
        })
@SpringBootApplication
@EnableFeignClients(basePackages = "ngvgroup.com.fac")

public class FacApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacApplication.class, args);
    }
}
