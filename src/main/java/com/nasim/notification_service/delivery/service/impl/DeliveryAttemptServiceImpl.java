package com.nasim.notification_service.delivery.service.impl;

import com.nasim.notification_service.delivery.service.DeliveryAttemptService;
import com.nasim.notification_service.model.entity.*;
import com.nasim.notification_service.repository.DeliveryAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryAttemptServiceImpl implements DeliveryAttemptService {


    private final DeliveryAttemptRepository deliveryAttemptRepository;

    public DeliveryAttemptServiceImpl(DeliveryAttemptRepository deliveryAttemptRepository) {
        this.deliveryAttemptRepository = deliveryAttemptRepository;
    }
    @Override
    public DeliveryAttempt create(Integer attemptNumber, NotificationRoute notificationRoute, String payloadJson){
        DeliveryAttempt deliveryAttempt=new DeliveryAttempt();
        deliveryAttempt.setAttemptNumber(attemptNumber );
        deliveryAttempt.setNotificationRoute(notificationRoute);
        deliveryAttempt.setStartedAt(LocalDateTime.now());
        deliveryAttempt.setSuccess(false);
        deliveryAttempt.setTenantID(notificationRoute.getTenantID());
        deliveryAttempt.setRequestPayloadJson(payloadJson);
        DeliveryAttempt savedAttempt= deliveryAttemptRepository.save(deliveryAttempt);
        return savedAttempt;
    }

    @Override
    public DeliveryAttempt updateStatus(DeliveryAttempt deliveryAttempt, boolean success, String errorCode, String errorMessage, boolean fetch) {
        if(fetch && deliveryAttempt!=null) deliveryAttempt= this.findById(deliveryAttempt.getId()) ;
        assert deliveryAttempt != null;
        deliveryAttempt.setSuccess(success);
        deliveryAttempt.setFinishedAt(LocalDateTime.now());
        deliveryAttempt.setErrorCode(errorCode);
        deliveryAttempt.setErrorMessage(errorMessage);
        return deliveryAttemptRepository.save(deliveryAttempt);
    }

    @Override
    public DeliveryAttempt findById(Long id) {
        return null;
    }


}


