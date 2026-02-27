package ngvgroup.com.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ngvgroup.com.bpm",
        "ngvgroup.com.loan",
        "com.ngvgroup.bpm.core.persistence",
})
@EnableFeignClients(basePackages = "ngvgroup.com.loan")
public class LoanApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SpringApplication.run(LoanApplication.class, args);
    }

}
