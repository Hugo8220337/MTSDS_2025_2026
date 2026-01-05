package com.domus.notifications.controllers;

import com.domus.notifications.dto.request.NotificationRequest;
import com.domus.notifications.dto.response.NotificationResponse;
import com.domus.notifications.models.Notification;
import com.domus.notifications.services.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final ModelMapper mapper;
    private final NotificationService svc;

    public NotificationController(ModelMapper mapper, NotificationService svc) {
        this.mapper = mapper;
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody NotificationRequest request) {
        Notification created = svc.createAndSend(request);
        NotificationResponse response = mapper.map(created, NotificationResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> get(@PathVariable String id) {
        Notification maybe = svc.findById(id);
        NotificationResponse response = mapper.map(maybe, NotificationResponse.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> list() {
        List<NotificationResponse> notifications = svc.findAll().stream()
                .map(e -> mapper.map(e, NotificationResponse.class))
                .toList();
        return ResponseEntity.ok(notifications);
    }
}




