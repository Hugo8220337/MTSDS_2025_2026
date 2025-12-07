package com.domus.notifications.models;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
public class Notification {
    @Id
    private String id;

    @Field(name = "user_id")
    private Long userId;

    private String email;

    private NotificationChannel channel;

    private String title;

    private String message;

    private NotificationStatus status;

    @Field("error")
    private String error;

    @CreatedDate
    @Field(name="created_at")
    private LocalDateTime createdAt;

    @Field(name="sent_at")
    private LocalDateTime sentAt;

    private Notification(Long userId, String email, NotificationChannel channel, String title, String message) {
        this.userId = userId;
        this.email = email;
        this.channel = channel;
        this.title = title;
        this.message = message;
        this.status = NotificationStatus.PENDING;
    }

    public static Notification createPending(Long userId, String email, NotificationChannel channel, String title, String message) {
        if (userId == null) throw new IllegalArgumentException("userId required");
        if (channel == null) throw new IllegalArgumentException("channel required");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title required");
        return new Notification(userId, email, channel, title, message);
    }

    public void markSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.error = null;
    }

    public void markFailed(String errorMessage) {
        this.status = NotificationStatus.FAILED;
        this.error = errorMessage;
    }
}

