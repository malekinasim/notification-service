package com.nasim.notification_service.routing.service.impl;

import com.nasim.notification_service.model.entity.BaseEntity;
import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationRoute;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.repository.NotificationRouteRepository;
import com.nasim.notification_service.routing.service.NotificationRouteService;
import com.nasim.notification_service.routing.service.RoutingPolicyStepService;
import com.nasim.notification_service.shared.exception.BusinessException;
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

    @Override
    public NotificationRoute updateStatus(NotificationRoute notificationRoute, NotificationRoute.RouteStatus routeStatus, Boolean fetch) {
        if(fetch && notificationRoute!=null ) notificationRoute=this.findByID(notificationRoute.getId());
        assert notificationRoute != null;
        notificationRoute.setStatus(routeStatus);
        return notificationRouteRepository.save(notificationRoute);
    }
    @Override
    public NotificationRoute findByID(Long notificationRouteId) {
        return notificationRouteRepository.findById(notificationRouteId).orElseThrow(
                ()-> new BusinessException("no_notificationRoute_found_by_id",notificationRouteId)
        );
    }

    @Override
    public List<NotificationRoute> updateAllStatus(List<NotificationRoute> notificationRoutes, NotificationRoute.RouteStatus routeStatus, boolean fetch) {
        if(fetch) notificationRoutes=notificationRouteRepository.findAllById(notificationRoutes.stream()
                .map(BaseEntity::getId).toList());
        for(NotificationRoute notificationRoute: notificationRoutes) {
            notificationRoute.setStatus(routeStatus);

        }
       return notificationRouteRepository.saveAll(notificationRoutes);

    }


}
