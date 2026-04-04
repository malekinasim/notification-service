package com.nasim.notification_service.shared.resourceBundel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
@Configuration
public class MassageResourceMng {
    @Bean
    ResourceBundleMessageSource messageSourceResourceBundle() {
        ResourceBundleMessageSource resourceBundle = new ResourceBundleMessageSource();
        resourceBundle.setBasename("messages");
        resourceBundle.setDefaultEncoding("UTF-8");
        resourceBundle.setUseCodeAsDefaultMessage(true);
        return resourceBundle;
    }
}
