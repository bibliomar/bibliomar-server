package bibliomar.bibliomarserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BibliomarServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliomarServerApplication.class, args);
    }

}
