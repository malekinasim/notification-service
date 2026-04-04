package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.RoutingPolicyStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutingPolicyStepRepository extends JpaRepository<RoutingPolicyStep, Long> {
    @Query(value = """
             select rpp from RoutingPolicyStep rpp
             join rpp.routingPolicy rp
             where rp.id= :routingPolicyId and rp.active=true and rpp.active= true
                        order by rpp.stepOrder
            """)
    List<RoutingPolicyStep> findAllByRoutingPolicyId(Long routingPolicyId);

}
