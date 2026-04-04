package com.nasim.notification_service.delivery.service.impl;

import com.nasim.notification_service.delivery.service.DeliveryAttemptService;
import com.nasim.notification_service.delivery.service.NotificationDeliveryService;
import com.nasim.notification_service.delivery.service.ProviderDispatcher;
import com.nasim.notification_service.model.dto.ProviderResponse;
import com.nasim.notification_service.model.entity.*;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueuedMessage;
import com.nasim.notification_service.notification.service.NotificationService;
import com.nasim.notification_service.routing.service.NotificationRouteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class NotificationDeliveryServiceImpl implements NotificationDeliveryService {
    private final NotificationRouteService notificationRouteService;
    private final NotificationService notificationService;
    private final DeliveryAttemptService deliveryAttemptService;
    private final ProviderDispatcher providerDispatcher;

    public NotificationDeliveryServiceImpl(NotificationRouteService notificationRouteService, NotificationService notificationService, DeliveryAttemptService deliveryAttemptService, ProviderDispatcher providerDispatcher) {
        this.notificationRouteService = notificationRouteService;
        this.notificationService = notificationService;
        this.deliveryAttemptService = deliveryAttemptService;
        this.providerDispatcher = providerDispatcher;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendQueuedMessageToProvider(NotificationQueuedMessage message) {
        Notification notification=notificationService.findbyIdAndStatus(message.notificationId(), Notification.NotificationStatus.QUEUED);
        notification=notificationService.updateNotificationStatus(notification, Notification.NotificationStatus.PROCESSING,"Delivery processing started",false);
        NotificationRoute notificationRoute=processNotificationDelivery(notification);
        Notification.NotificationStatus status= Notification.NotificationStatus.SENT;

        if(notificationRoute==null || notificationRoute.getStatus().name().equals(NotificationRoute.RouteStatus.FAILED.name())){
            status= Notification.NotificationStatus.FAILED;
        }
        notificationService.updateNotificationStatus(notification,status,String.format("Delivery process finished %s",notificationRoute.getStatus().name()),false);
    }

    private NotificationRoute processNotificationDelivery(Notification notification) {
        List<NotificationRoute> pendingRoutes =
                notificationRouteService.findAllNotificationRoutByNotificationIdAndStatus(
                        notification.getId(),
                        NotificationRoute.RouteStatus.PENDING
                );
        if(CollectionUtils.isEmpty(pendingRoutes)) return null;
        int idx = 0;
        NotificationRoute notificationRoute = null;
        for (; idx < pendingRoutes.size(); idx++) {
            notificationRoute = pendingRoutes.get(idx);
            RoutingPolicyStep routingPolicyStep = notificationRoute.getRoutingPolicyStep();
            RoutingPolicy routingPolicy = routingPolicyStep.getRoutingPolicy();
            notificationRoute = notificationRouteService.updateStatus(notificationRoute,  NotificationRoute.RouteStatus.PROCESSING, false);
            DeliveryAttempt deliveryAttempt = processRoutingStep(notification, notificationRoute, routingPolicyStep, routingPolicy);
            if (deliveryAttempt.isSuccess()) {
                notificationRoute = notificationRouteService.updateStatus(notificationRoute,  NotificationRoute.RouteStatus.SENT, false);
                if (idx + 1 < pendingRoutes.size()) {
                    notificationRouteService.updateAllStatus(pendingRoutes.subList(idx + 1, pendingRoutes.size()), NotificationRoute.RouteStatus.SKIPPED, false);
                }
                break;
            } else {
                notificationRoute = notificationRouteService.updateStatus(notificationRoute,  NotificationRoute.RouteStatus.FAILED, false);


            }

        }

        return notificationRoute;

    }

    private DeliveryAttempt processRoutingStep(Notification notification,NotificationRoute notificationRoute, RoutingPolicyStep routingPolicyStep,
                                    RoutingPolicy routingPolicy) {
        DeliveryAttempt deliveryAttempt = null;
        for (int retryNum = 0; retryNum < routingPolicyStep.getMaxRetry(); retryNum++) {
            deliveryAttempt = deliveryAttemptService.create(retryNum + 1, notificationRoute, notification.getPayloadJson());
            ProviderResponse providerResponse = providerDispatcher.dispatchMessageToProvider(notification, notificationRoute, routingPolicy, routingPolicyStep, deliveryAttempt);
            if (providerResponse.success()) {
                deliveryAttemptService.updateStatus(deliveryAttempt,true,null,null,false);
                break;
            } else {
                deliveryAttemptService.updateStatus(deliveryAttempt,false, String.valueOf(providerResponse.errorCode()),providerResponse.errorMessage(),false);

            }
        }
        return deliveryAttempt;

    }



}
