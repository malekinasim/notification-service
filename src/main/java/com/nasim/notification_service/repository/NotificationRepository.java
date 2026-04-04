package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = """ 
                    select nt from Notification nt where nt.currentStatus= :staus
                        order by nt.createdAt Asc limit 50
            """)
    List<Notification> findAllByStatus(Notification.NotificationStatus status);


    @Query(value = """ 
                    select nt from Notification nt where nt.currentStatus=:staus
                       and nt.id= :id
            """)
    Optional<Notification> findByIdAndStatus(Long id, Notification.NotificationStatus status);
}