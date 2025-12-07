package com.domus.notifications.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private Long userId;
    private String email;
    private String channel;
    private String title;
    private String message;
    private String status;
    private String error;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}

