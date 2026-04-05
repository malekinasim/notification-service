package com.nasim.notification_service.render.service;

import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.Template;

public interface NotificationMessageRendererService {
    String render(Template template, String payloadJson);
    String render(Notification notification);
}
