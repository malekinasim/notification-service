package com.nasim.notification_service.provider.service;

import com.nasim.notification_service.model.dto.ProviderDispatchCommand;
import com.nasim.notification_service.model.dto.ProviderSendResult;
import org.springframework.stereotype.Component;

@Component
public class ProviderDispatcher {
    public ProviderSendResult dispatchMessageToProvider(ProviderDispatchCommand command) {

       return new ProviderSendResult(true,"1",
               "success","201","success");
    }
}
