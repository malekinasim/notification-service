package com.nasim.notification_service.service;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.NotificationRoute;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import java.util.List;
public interface NotificationRoutService {

    List<NotificationRoute> generateRoutingPlan(Notification savedNotification, RoutingPolicy routingPolicy);
}
