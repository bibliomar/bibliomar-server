package bibliomar.bibliomarserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        "http://localhost:3001",
                        "http://localhost:5173",
                        "https://bibliomar.netlify.app",
                        "https://bibliomar.com",
                        "https://www.bibliomar.com",
                        "https://beta.bibliomar.com",
                        "https://bibliomar.site",
                        "https://www.bibliomar.site",
                        "https://beta.bibliomar.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
