package com.nasim.notification_service.service.impl;

import com.nasim.notification_service.model.entity.RoutingPolicyStep;
import com.nasim.notification_service.repository.RoutingPolicyStepRepository;
import com.nasim.notification_service.service.RoutingPolicyStepService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutingPolicyStepServiceImpl implements RoutingPolicyStepService {

    private final RoutingPolicyStepRepository routingPolicyStepRepository;

    public RoutingPolicyStepServiceImpl(RoutingPolicyStepRepository routingPolicyStepRepository) {
        this.routingPolicyStepRepository = routingPolicyStepRepository;
    }
    @Override
    public List<RoutingPolicyStep> findAllStepOfRoutingPolicy(Long routingPolicyId){
        return routingPolicyStepRepository.findAllByRoutingPolicyId(routingPolicyId);
    }
}
