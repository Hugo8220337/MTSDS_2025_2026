package com.domus.assessments.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendNotificationMessage {
    private Long userId;
    private String email;
    private String title;
    private String message;
    private String channel;
}