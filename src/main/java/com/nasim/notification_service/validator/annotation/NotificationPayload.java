
package com.nasim.notification_service.validator.annotation;

import com.nasim.notification_service.validator.NotificationPayloadValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotificationPayloadValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotificationPayload {
    String message() default "Notification payload is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
