package com.nasim.notification_service.routing.service.impl;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationRoute;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.repository.NotificationRouteRepository;
import com.nasim.notification_service.routing.service.NotificationRouteService;
import com.nasim.notification_service.routing.service.RoutingPolicyStepService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationRouteServiceImpl implements NotificationRouteService {
    private final RoutingPolicyStepService routingPolicyStepService;
    private final NotificationRouteRepository notificationRouteRepository;

    public NotificationRouteServiceImpl(RoutingPolicyStepService routingPolicyStepService, NotificationRouteRepository notificationRouteRepository) {
        this.routingPolicyStepService = routingPolicyStepService;
        this.notificationRouteRepository = notificationRouteRepository;
    }

    @Override
    public List<NotificationRoute> generateRoutingPlan(Notification savedNotification, RoutingPolicy routingPolicy) {
        List<NotificationRoute> routes = routingPolicyStepService.findAllStepOfRoutingPolicy(routingPolicy.getId()).stream().map(
                routingPolicyStep -> {
                    NotificationRoute notificationRoute = new NotificationRoute();
                    notificationRoute.setNotification(savedNotification);
                    notificationRoute.setRoutingPolicyStep(routingPolicyStep);
                    notificationRoute.setStatus(NotificationRoute.RouteStatus.PENDING);
                    return notificationRoute;
                }
        ).toList();

        return notificationRouteRepository.saveAll(routes);
    }

    @Override
    public List<NotificationRoute> findAllNotificationRoutByNotificationIdAndStatus(Long notificationId, NotificationRoute.RouteStatus routeStatus) {
        return notificationRouteRepository.findAllNotificationRoutByNotificationIdAndStatus(notificationId, routeStatus);
    }
}
