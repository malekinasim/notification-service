package com.nasim.notification_service.notification.service.impl;

import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.notification.service.TemplateService;
import com.nasim.notification_service.repository.TemplateRepository;
import com.nasim.notification_service.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {
    private final TemplateRepository templateRepository;

    public TemplateServiceImpl(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Override
    public Template findByCode(String code) {
        return templateRepository.findByCodeAndActiveTrue(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Template not found: " + code
                        )
                );
    }
}
