package com.domus.notifications.repositories;

import com.domus.notifications.models.Notification;
import com.domus.notifications.models.NotificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringNotificationRepository extends MongoRepository<Notification, Long> {
    List<Notification> findByStatus(NotificationStatus status);
}