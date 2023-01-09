package bibliomar.bibliomarserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan
@EnableAsync
public class BibliomarServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliomarServerApplication.class, args);
	}

}
