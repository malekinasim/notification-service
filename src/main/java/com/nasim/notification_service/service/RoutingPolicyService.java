package com.nasim.notification_service.service;

import com.nasim.notification_service.model.entity.RoutingPolicy;

public interface RoutingPolicyService {
    RoutingPolicy resolveRoutingPolicy(String templateCode);
}