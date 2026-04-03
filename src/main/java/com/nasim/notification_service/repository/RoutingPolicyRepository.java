package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.RoutingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoutingPolicyRepository extends JpaRepository<RoutingPolicy, Long> {

    @Query("""
        select rp
        from RoutingPolicy rp
        join rp.template t
        where t.code = :templateCode
          and rp.tenantId = :tenantId
          and rp.active = true
    """)
    Optional<RoutingPolicy> findByTemplateCodeAndTenantId(String templateCode, String tenantId);

    @Query("""
        select rp
        from RoutingPolicy rp
        join rp.template t
        where t.code = :templateCode
          and rp.tenantId is null
          and rp.defaultPolicy = true
          and rp.active = true
    """)
    Optional<RoutingPolicy> findDefaultByTemplateCode(String templateCode);
}