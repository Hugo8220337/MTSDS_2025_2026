package com.domus.notifications.repositories;

import com.domus.notifications.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}

