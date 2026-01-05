package com.domus.notifications.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private String email;
    private String channel; // "EMAIL" | "PUSH" | "SMS"
    private String title;
    private String message;
}

