package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.NotificationRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRouteRepository extends JpaRepository<NotificationRoute, Long> {


    @Query(value = """
             select ntr from NotificationRoute ntr
             join ntr.notification nt
             join ntr.routingPolicyStep rps
             join rps.routingPolicy rp
             where nt.id= :notificationId and ntr.status= :routeStatus
             order by rps.stepOrder
            
            """)
    List<NotificationRoute> findAllNotificationRoutByNotificationIdAndStatus(Long notificationId, NotificationRoute.RouteStatus routeStatus);
}
