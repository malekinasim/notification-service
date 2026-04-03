package com.nasim.notification_service.service.impl;

import com.nasim.notification_service.config.tenant.TenantContext;
import com.nasim.notification_service.exception.BusinessException;
import com.nasim.notification_service.exception.TenantResolutionException;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.repository.RoutingPolicyRepository;
import com.nasim.notification_service.service.RoutingPolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
public class RoutingPolicyServiceImpl implements RoutingPolicyService {

    private final RoutingPolicyRepository routingPolicyRepository;

    public RoutingPolicyServiceImpl(RoutingPolicyRepository routingPolicyRepository) {
        this.routingPolicyRepository = routingPolicyRepository;
    }

    @Override
    public RoutingPolicy resolveRoutingPolicy(String templateCode) {
        String tenantId = TenantContext.getTenantId();

        if (!StringUtils.hasText(tenantId)) {
            throw new TenantResolutionException("Tenant could not be resolved");
        }

        return routingPolicyRepository
                .findByTemplateCodeAndTenantId(templateCode, tenantId)
                .orElseGet(() ->
                        routingPolicyRepository
                                .findDefaultByTemplateCode(templateCode)
                                .orElseThrow(() ->
                                        new BusinessException("No_routing_policy_found", templateCode)
                                )
                );
    }
}