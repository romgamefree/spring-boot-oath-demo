package dev.danvega.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Cấu hình Spring MVC tập trung: resource handlers, view resolvers,
 * interceptors, và CORS.
 *
 * <p>Thêm interceptor mới tại đây thay vì dùng {@code @Component} rải rác:</p>
 * <pre>{@code
 * @Override
 * public void addInterceptors(InterceptorRegistry registry) {
 *     registry.addInterceptor(new MyInterceptor())
 *             .addPathPatterns("/admin/**");
 * }
 * }</pre>
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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

    /**
     * CORS: chỉ cần cho REST API endpoint.
     * Nếu dự án là full MVC (không có JS client riêng), có thể bỏ.
     *
     * <p>Bật lại khi cần: uncomment và điều chỉnh origin cho phù hợp môi trường.</p>
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Mặc định không cần CORS vì cùng origin (JTE render server-side)
        // Uncomment nếu có REST API cho external client:
        //
        // registry.addMapping("/api/**")
        //         .allowedOrigins("https://your-frontend.com")
        //         .allowedMethods("GET", "POST", "PUT", "DELETE")
        //         .allowedHeaders("*")
        //         .allowCredentials(true)
        //         .maxAge(3600);
    }

    /**
     * Path matching: dùng AntPathMatcher mặc định của Spring.
     * Đã đủ cho hầu hết use case.
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Không suffix pattern matching (deprecated từ Spring 5.3)
        // URL rõ ràng hơn, ví dụ /articles thay vì /articles.html
    }
}
