package com.nasim.notification_service.validator;

import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.model.entity.TemplateVariable;
import com.nasim.notification_service.notification.service.TemplateService;
import com.nasim.notification_service.notification.service.TemplateVariableService;
import com.nasim.notification_service.validator.annotation.NotificationPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.GenericValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class NotificationPayloadValidator
        implements ConstraintValidator<NotificationPayload, NotificationDto> {

    private final TemplateService templateService;
    private final TemplateVariableService templateVariableService;

    public NotificationPayloadValidator(
            TemplateService templateService,
            TemplateVariableService templateVariableService
    ) {
        this.templateService = templateService;
        this.templateVariableService = templateVariableService;
    }

    @Override
    public boolean isValid(NotificationDto notificationDto, ConstraintValidatorContext context) {
        if (notificationDto == null) {
            return true;
        }

        if (!StringUtils.hasText(notificationDto.templateCode())
                || !StringUtils.hasText(notificationDto.payloadJson())) {
            return false;
        }

        Template template = templateService.findByCode(notificationDto.templateCode());
        if (template == null) {
            buildViolation(context, "Template not found", "templateCode");
            return false;
        }

        JSONObject payloadObject;
        try {
            payloadObject = new JSONObject(notificationDto.payloadJson());
        } catch (JSONException ex) {
            buildViolation(context, "payloadJson is not valid JSON", "payloadJson");
            return false;
        }

        List<TemplateVariable> templateVariables =
                templateVariableService.findAllTemplateVariableByTemplateId(template.getId());

        for (TemplateVariable templateVariable : templateVariables) {
            String variableName = templateVariable.getName();

            if (templateVariable.isRequired() && !payloadObject.has(variableName)) {
                buildViolation(
                        context,
                        "Missing required variable: " + variableName,
                        "payloadJson"
                );
                return false;
            }

            if (!payloadObject.has(variableName)) {
                continue;
            }

            Object value = payloadObject.get(variableName);

            if (templateVariable.isRequired()
                    && (value == null || !StringUtils.hasText(value.toString()))) {
                buildViolation(
                        context,
                        "Variable is empty: " + variableName,
                        "payloadJson"
                );
                return false;
            }

            if (!validateDataType(templateVariable.getDataType(), value == null ? null : value.toString())) {
                buildViolation(
                        context,
                        "Invalid value for variable: " + variableName,
                        "payloadJson"
                );
                return false;
            }
        }

        return true;
    }

    private void buildViolation(
            ConstraintValidatorContext context,
            String message,
            String property
    ) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }

    private boolean validateDataType(
            TemplateVariable.TemplateVariableDataType dataType,
            String value
    ) {
        if (value == null) {
            return false;
        }

        return switch (dataType) {
            case BOOLEAN -> value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            case DATE -> GenericValidator.isDate(value, "yyyy-MM-dd", true);
            case NUMBER -> isNumber(value);
            case JSON -> isJsonValid(value);
            case STRING -> true;
        };
    }

    private boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private boolean isJsonValid(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
                return true;
            } catch (JSONException ex1) {
                return false;
            }
        }
    }
}