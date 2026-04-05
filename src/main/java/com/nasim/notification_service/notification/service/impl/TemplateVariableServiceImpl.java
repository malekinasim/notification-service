package com.nasim.notification_service.notification.service.impl;

import com.nasim.notification_service.model.entity.TemplateVariable;
import com.nasim.notification_service.notification.service.TemplateVariableService;
import com.nasim.notification_service.repository.TemplateVariableRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TemplateVariableServiceImpl implements TemplateVariableService {
    private final TemplateVariableRepository templateVariableRepository;

    public TemplateVariableServiceImpl(TemplateVariableRepository templateVariableRepository) {
        this.templateVariableRepository = templateVariableRepository;
    }

    @Override
    public List<TemplateVariable> findAllTemplateVariableByTemplateId(Long templateId) {
        return templateVariableRepository.findAllTemplateVariableByTemplateId(templateId);
    }
}
