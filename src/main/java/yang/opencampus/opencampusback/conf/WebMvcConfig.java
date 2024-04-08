package yang.opencampus.opencampusback.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://jufeopencampus.club")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Cookie")
                .allowCredentials(true)
                .maxAge(3600); // 设置预检请求的有效期，单位为秒
    }
}

