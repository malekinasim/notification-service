package com.nasim.notification_service.routing.service;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationRoute;
import com.nasim.notification_service.model.entity.RoutingPolicy;

import java.util.List;

public interface NotificationRouteService {

    List<NotificationRoute> generateRoutingPlan(Notification savedNotification, RoutingPolicy routingPolicy);

    List<NotificationRoute> findAllNotificationRoutByNotificationIdAndStatus(Long notificationId, NotificationRoute.RouteStatus routeStatus);

    NotificationRoute updateStatus(NotificationRoute notificationRoute, NotificationRoute.RouteStatus routeStatus, Boolean fetch);

    NotificationRoute findByID(Long notificationRouteId);

    List<NotificationRoute> updateAllStatus(List<NotificationRoute> notificationRoutes, NotificationRoute.RouteStatus routeStatus, boolean b);
}
