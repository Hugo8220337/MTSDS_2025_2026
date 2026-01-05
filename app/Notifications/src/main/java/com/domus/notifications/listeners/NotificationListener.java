package com.domus.notifications.listeners;

import com.domus.notifications.configs.MQConfig;
import com.domus.notifications.dto.request.NotificationRequest;
import com.domus.notifications.messages.NotificationEvent;
import com.domus.notifications.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {
    private final ModelMapper mapper;
    private final NotificationService notificationService;

    public NotificationListener(ModelMapper mapper, NotificationService notificationService) {
        this.mapper = mapper;
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiveNotification(NotificationEvent event) {
        log.info("Received notification event {}", event);

        NotificationRequest request = mapper.map(event, NotificationRequest.class);
        notificationService.createAndSend(request);
    }
}
