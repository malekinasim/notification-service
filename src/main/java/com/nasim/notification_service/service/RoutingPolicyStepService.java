package com.nasim.notification_service.service;

import com.nasim.notification_service.model.entity.RoutingPolicyStep;

import java.util.List;

public interface RoutingPolicyStepService {
    List<RoutingPolicyStep> findAllStepOfRoutingPolicy(Long routingPolicyId);
}
