package com.nasim.notification_service.delivery.service.impl;

import com.nasim.notification_service.delivery.service.DeliveryAttemptService;
import com.nasim.notification_service.delivery.service.NotificationDeliveryService;
import com.nasim.notification_service.provider.service.ProviderDispatcher;
import com.nasim.notification_service.model.dto.ProviderDispatchCommand;
import com.nasim.notification_service.model.dto.ProviderSendResult;
import com.nasim.notification_service.model.entity.*;
import com.nasim.notification_service.notification.kafka.payload.NotificationQueuedMessage;
import com.nasim.notification_service.notification.service.NotificationService;
import com.nasim.notification_service.render.service.NotificationMessageRendererService;
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
    private final NotificationMessageRendererService notificationMessageRendererService;

    public NotificationDeliveryServiceImpl(NotificationRouteService notificationRouteService, NotificationService notificationService, DeliveryAttemptService deliveryAttemptService, ProviderDispatcher providerDispatcher, NotificationMessageRendererService notificationMessageRendererService) {
        this.notificationRouteService = notificationRouteService;
        this.notificationService = notificationService;
        this.deliveryAttemptService = deliveryAttemptService;
        this.providerDispatcher = providerDispatcher;
        this.notificationMessageRendererService = notificationMessageRendererService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendQueuedMessageToProvider(NotificationQueuedMessage message) {
        Notification notification=notificationService.findbyIdAndStatus(message.notificationId(), Notification.NotificationStatus.QUEUED);
        notification=notificationService.updateNotificationStatus(notification, Notification.NotificationStatus.PROCESSING,"Delivery processing started",false);
        Boolean  finalRouteStatus=processNotificationDelivery(notification);
        Notification.NotificationStatus status= Notification.NotificationStatus.SENT;

        if(!finalRouteStatus ){
            status= Notification.NotificationStatus.FAILED;
        }
        notificationService.updateNotificationStatus(notification,status,String.format("Delivery process finished %s",status.name()),false);
    }

    private Boolean processNotificationDelivery(Notification notification) {
        List<NotificationRoute> pendingRoutes =
                notificationRouteService.findAllNotificationRoutByNotificationIdAndStatus(
                        notification.getId(),
                        NotificationRoute.RouteStatus.PENDING
                );
        if(CollectionUtils.isEmpty(pendingRoutes)) return false;
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

        return notificationRoute.getStatus().name().equals(NotificationRoute.RouteStatus.FAILED.name())? false : true;

    }

    private DeliveryAttempt processRoutingStep(Notification notification,NotificationRoute notificationRoute, RoutingPolicyStep routingPolicyStep,
                                    RoutingPolicy routingPolicy) {
        DeliveryAttempt deliveryAttempt = null;
        for (int retryNum = 0; retryNum < routingPolicyStep.getMaxRetry(); retryNum++) {
            deliveryAttempt = deliveryAttemptService.create(retryNum + 1, notificationRoute, notification.getPayloadJson());

            String body=notificationMessageRendererService.render(notification.getTemplate(),notification.getPayloadJson());
            ProviderSendResult providerSendResult = providerDispatcher.dispatchMessageToProvider(
                    new ProviderDispatchCommand(
                            notification.getId(),
                            notificationRoute.getId(),
                            notification.getTenantID(),
                            routingPolicyStep.getProviderChannel().getChannel().getType(),
                            routingPolicyStep.getProviderChannel().getProvider().getType(),
                            notification.getRecipientAddress(),
                            notification.getRecipientName(),
                            notification.getSenderAddress(),
                            notification.getSenderName(),
                            body,
                            notification.getTemplate().getContentType()
                    )
            );
            if (providerSendResult.success()) {
                deliveryAttemptService.updateStatus(deliveryAttempt,true,null,null,false);
                break;
            } else {
                deliveryAttemptService.updateStatus(deliveryAttempt,false, providerSendResult.errorCode(), providerSendResult.errorMessage(),false);

            }
        }
        return deliveryAttempt;

    }



}
