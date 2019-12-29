package carzanodev.genuniv.microservices.precord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PersonalRecordsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalRecordsServiceApplication.class, args);
    }

}
