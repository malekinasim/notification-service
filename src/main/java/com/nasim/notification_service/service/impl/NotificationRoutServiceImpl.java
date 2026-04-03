package com.nasim.notification_service.service.impl;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationRoute;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.repository.NotificationRouteRepository;
import com.nasim.notification_service.service.NotificationRoutService;
import com.nasim.notification_service.service.RoutingPolicyStepService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationRoutServiceImpl implements NotificationRoutService {
    private final RoutingPolicyStepService routingPolicyStepService;
    private final NotificationRouteRepository notificationRouteRepository;

    public NotificationRoutServiceImpl(RoutingPolicyStepService routingPolicyStepService, NotificationRouteRepository notificationRouteRepository) {
        this.routingPolicyStepService = routingPolicyStepService;
        this.notificationRouteRepository = notificationRouteRepository;
    }

    @Override
    public List<NotificationRoute> generateRoutingPlan(Notification savedNotification, RoutingPolicy routingPolicy) {
        List<NotificationRoute> routes=routingPolicyStepService.findAllStepOfRoutingPolicy(routingPolicy.getId()).stream().map(
                routingPolicyStep -> {
                    NotificationRoute notificationRoute=new NotificationRoute();
                    notificationRoute.setNotification(savedNotification);
                    notificationRoute.setRoutingPolicyStep(routingPolicyStep);
                    notificationRoute.setStatus(NotificationRoute.RouteStatus.PENDING);
                    return notificationRoute;
                }
        ).toList();

        return notificationRouteRepository.saveAll(routes);
    }
}
