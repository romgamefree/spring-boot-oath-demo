package dev.danvega.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FlashMessageInterceptor flashMessageInterceptor;

    public WebMvcConfig(FlashMessageInterceptor flashMessageInterceptor) {
        this.flashMessageInterceptor = flashMessageInterceptor;
    }

    /**
     * Static resources: cache 1 năm trong production, không cache trong dev.
     * Spring Boot đã tự cấu hình /static, /public, /resources, /META-INF/resources.
     * Override ở đây nếu cần custom resource location.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Favicon
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(86400); // 1 ngày
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(flashMessageInterceptor);
    }
}
