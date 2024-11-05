package tingeso.prestabanco.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("http://localhost:5173",
                                "http://104.41.28.220:5173",
                                "http://10.1.0.4:5173") // Allow your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow methods
                .allowedHeaders("*"); // Allow all headers
    }
}