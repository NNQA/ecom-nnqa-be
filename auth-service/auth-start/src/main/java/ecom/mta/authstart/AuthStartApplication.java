package ecom.mta.authstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ecom.mta")
public class AuthStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthStartApplication.class, args);
    }
}
