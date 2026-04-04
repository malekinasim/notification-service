package com.nasim.notification_service.routing.service;

import com.nasim.notification_service.model.entity.RoutingPolicy;

public interface RoutingPolicyService {
    RoutingPolicy resolveRoutingPolicy(String templateCode);
}