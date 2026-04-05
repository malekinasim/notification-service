package com.nasim.notification_service.validator;

import ch.qos.logback.core.util.StringUtil;
import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.model.entity.TemplateVariable;
import com.nasim.notification_service.notification.service.TemplateService;
import com.nasim.notification_service.notification.service.TemplateVariableService;
import com.nasim.notification_service.validator.annotation.NotificationPayload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.validator.GenericValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationPayloadValidator implements ConstraintValidator<NotificationPayload, NotificationDto> {
    private final TemplateService templateService;
    private final TemplateVariableService templateVariableService;

    public NotificationPayloadValidator(TemplateService templateService, TemplateVariableService templateVariableService) {
        this.templateService = templateService;
        this.templateVariableService = templateVariableService;
    }

    @Override
    public boolean isValid(NotificationDto notificationDto, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtil.isNullOrEmpty(notificationDto.payloadJson()) || StringUtil.isNullOrEmpty(notificationDto.templateCode()))
            return false;
        Template template = templateService.findByCode(notificationDto.templateCode());
        if (template == null)
            return false;
        List<TemplateVariable> templateVariables = templateVariableService.findAllTemplateVariableByTemplateId(template.getId());

        boolean isValid = isValid(template, templateVariables, notificationDto.payloadJson());
        if (!isValid) {
            //disable default validator on class
            constraintValidatorContext.disableDefaultConstraintViolation();

            // enable validator on payloadJson field
            constraintValidatorContext.buildConstraintViolationWithTemplate("Notification payload doesn't match with template ")
                    .addPropertyNode("payloadJson")
                    .addConstraintViolation();
        }
        return isValid;
    }

    private boolean isValid(Template template, List<TemplateVariable> templateVariables, @NotBlank(message = "payloadJson must not be blank") String payloadJson) {
        JSONObject jsonObject =new JSONObject();
        try {
             jsonObject = new JSONObject(payloadJson);
        } catch (JSONException ex) {
            return false;
        }
        for (TemplateVariable templateVariable : templateVariables) {
            if (templateVariable.isRequired() && !jsonObject.has(templateVariable.getName()))
                return false;

            if (jsonObject.has(templateVariable.getName())) {
                Object value = jsonObject.get(templateVariable.getName());
                if (templateVariable.isRequired() && (value == null || StringUtil.isNullOrEmpty(value.toString())))
                    return false;
               Boolean isValid= validateDataType(templateVariable.getDataType(), value.toString());
               if(!isValid)
                   return  false;

            }

        }
        return true;
    }

    private Boolean validateDataType(TemplateVariable.TemplateVariableDataType dataType, String value) {

        return switch (dataType) {
            case BOOLEAN -> {
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    yield true;
                } else {
                    yield false;
                }
            }
            case DATE -> {
                yield GenericValidator.isDate(value, "yyyy-MM-dd", true);
            }
            case NUMBER -> {
                try {
                    Double.parseDouble(value);
                    yield true;
                } catch (NumberFormatException ignored) {
                    yield false;
                }
            }
            case JSON -> {
                yield isJsonValid(value);
            }
            case STRING -> {
                yield true;
            }
            default -> throw new IllegalArgumentException("invalid variable dataType " + dataType);
        };
    }

    private boolean isJsonValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
