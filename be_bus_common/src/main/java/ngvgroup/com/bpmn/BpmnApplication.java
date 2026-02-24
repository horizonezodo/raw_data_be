package ngvgroup.com.bpmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ngvgroup.com.bpmn", "lib.ngvgroup.bpmn" })
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(basePackages = "ngvgroup.com.bpmn")

public class BpmnApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmnApplication.class, args);
	}

}
