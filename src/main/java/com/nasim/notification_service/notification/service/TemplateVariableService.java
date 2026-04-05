package com.nasim.notification_service.notification.service;

import com.nasim.notification_service.model.entity.TemplateVariable;

import java.util.List;

public interface TemplateVariableService {
    List<TemplateVariable> findAllTemplateVariableByTemplateId(Long templateId);
}
