package com.nasim.notification_service.model.dto;


import com.nasim.notification_service.validator.annotation.NotificationPayload;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
@NotificationPayload(message = "notification data is invalid")
public record NotificationDto(
        Long tempLateId,
        @NotBlank(message = "templateCode must not be blank")
        String templateCode,
        String routingPolicyCode,
        @NotBlank(message = "recipientAddress must not be blank")
        String recipientAddress,
        String recipientName,
        String senderAddress,
        String senderName,
        @NotBlank(message = "payloadJson must not be blank")
        String payloadJson,
        String currentStatus,
        LocalDateTime createdDate
) {

}
