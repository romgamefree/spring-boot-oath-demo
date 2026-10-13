package dev.danvega.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Cấu hình async execution với ThreadPool được tune cho ứng dụng web.
 *
 * <p>Sau khi config này active, dùng {@code @Async} trong Service để chạy
 * background tasks mà không block HTTP thread:</p>
 *
 * <pre>{@code
 * @Service
 * public class EmailService {
 *
 *     @Async
 *     public CompletableFuture<Void> sendWelcomeEmail(String to) {
 *         // chạy trong background thread pool
 *         emailClient.send(to, "Chào mừng!");
 *         return CompletableFuture.completedFuture(null);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Lưu ý:</b> Method {@code @Async} phải được gọi từ bean khác (không phải
 * {@code this.method()}) để Spring proxy có thể intercept.</p>
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * Thread pool chính cho {@code @Async} tasks.
     *
     * <p>Cấu hình:</p>
     * <ul>
     *   <li>Core pool: số CPU cores → đủ cho I/O-bound tasks</li>
     *   <li>Max pool: core × 4 → burst capacity</li>
     *   <li>Queue: 100 → buffer trước khi reject</li>
     *   <li>Timeout: 60s → giải phóng idle thread</li>
     * </ul>
     */
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(cores);
        executor.setMaxPoolSize(cores * 4);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();

        log.info("AsyncConfig: thread pool khởi tạo (core={}, max={}, queue=100)",
                cores, cores * 4);
        return executor;
    }

    /**
     * Xử lý exception từ {@code @Async} void methods.
     * Exception từ CompletableFuture được handle bởi caller.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Lỗi async trong {}.{}(): {}",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(),
                        ex.getMessage(), ex);
    }
}
