package dev.danvega.service;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Retryable(maxRetries = 3, delay = 1000)
    public void sendNotification() {
        // Gọi dịch vụ gửi email
    }
}