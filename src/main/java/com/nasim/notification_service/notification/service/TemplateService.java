package com.nasim.notification_service.notification.service;

import com.nasim.notification_service.model.entity.Template;

public interface TemplateService {
    Template findByCode(String code);
}
