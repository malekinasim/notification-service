package com.nasim.notification_service.render.service.impl;

import com.nasim.notification_service.render.service.NotificationMessageRendererService;
import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.model.entity.TemplateVariable;
import com.nasim.notification_service.notification.service.TemplateVariableService;
import com.nasim.notification_service.shared.exception.BusinessException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public  class NotificationMessageRendererServiceImpl implements NotificationMessageRendererService {

    private final TemplateVariableService templateVariableService;

    public NotificationMessageRendererServiceImpl(TemplateVariableService templateVariableService) {
        this.templateVariableService = templateVariableService;
    }
    @Override
    public String render(Template template, String payloadJson) {

        // 1. validate template
        if (template == null || !StringUtils.hasText(template.getBodyTemplate())) {
            throw new BusinessException("Invalid_template");
        }

        // 2. parse payload
        JSONObject payloadObject;
        try {
            payloadObject = new JSONObject(payloadJson);
        } catch (JSONException ex) {
            throw new BusinessException("Invalid_payload_json");
        }

        // 3. get variables
        List<TemplateVariable> variables =
                templateVariableService.findAllTemplateVariableByTemplateId(template.getId());

        String content = template.getBodyTemplate();

        // 4. replace variables
        for (TemplateVariable variable : variables) {

            String key = variable.getName();
            String value;

            if (payloadObject.has(key)) {
                value = payloadObject.get(key).toString();

                // optional: empty check
                if (variable.isRequired() && !StringUtils.hasText(value)) {
                    throw new BusinessException("Variable_empty", key);
                }

            } else if (variable.getDefaultValue() != null) {
                value = variable.getDefaultValue();

            } else {
                throw new BusinessException("Missing_variable", key);
            }

            content = content.replace("{{" + key + "}}", value);
        }

        return content;
    }

    @Override
    public String render(Notification notification) {
        return "";
    }
}