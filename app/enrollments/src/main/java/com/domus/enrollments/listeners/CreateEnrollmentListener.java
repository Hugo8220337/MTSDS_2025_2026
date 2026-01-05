package com.domus.enrollments.listeners;

import com.domus.enrollments.configs.MQConfig;
import com.domus.enrollments.dto.request.EnrollmentRequestDTO;
import com.domus.enrollments.messages.CreateEnrollmentMessage;
import com.domus.enrollments.services.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateEnrollmentListener {

    private final ModelMapper modelMapper;
    private final EnrollmentService enrollmentService;

    public CreateEnrollmentListener(ModelMapper modelMapper, EnrollmentService enrollmentService) {
        this.modelMapper = modelMapper;
        this.enrollmentService = enrollmentService;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CreateEnrollmentMessage message) {
        if (message == null) {
            log.warn("Received null CreateEnrollmentMessage, ignoring.");
            return;
        }

        if (message.getNif() == null || message.getNif().isBlank()) {
            log.warn("Missing nif in message: {}", message);
            return;
        }

        var enrollmentDTO = modelMapper.map(message, EnrollmentRequestDTO.class);
        enrollmentService.createEnrollment(enrollmentDTO);
    }
}
