package com.domus.notifications.services;

import com.domus.notifications.dto.request.NotificationRequest;
import com.domus.notifications.exceptions.EntityNotFoundException;
import com.domus.notifications.models.Notification;
import com.domus.notifications.models.NotificationChannel;
import com.domus.notifications.repositories.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class NotificationService {
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Notification createAndSend(NotificationRequest request) {
        Long userId = request.getUserId();
        String email = request.getEmail();
        NotificationChannel channel = NotificationChannel.valueOf(request.getChannel().toUpperCase());
        String title = request.getTitle();
        String message = request.getMessage();

        Notification newNotification = Notification.createPending(userId, email, channel, title, message);

        // Save Notification
        newNotification = repository.save(newNotification);

        // Update Notification to be marked as sent, in case of error is marked as failed
        try {
            newNotification.markSent();
            repository.save(newNotification);
            log.info("Notification {} sent", newNotification.getId());
        } catch (Exception ex) {
            String err = ex.getMessage() != null ? ex.getMessage() : ex.toString();
            newNotification.markFailed(err);
            repository.save(newNotification);
            log.error("Failed to send notification {}: {}", newNotification.getId(), err, ex);
        }

        return newNotification;
    }

    public Notification findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification not found"));
    }

    @Transactional(readOnly = true)
    public List<Notification> findAll() {
        return repository.findAll();
    }
}

