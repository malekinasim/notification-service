package com.nasim.notification_service.notification.service.impl;

import com.nasim.notification_service.config.tenant.TenantContext;
import com.nasim.notification_service.shared.exception.BusinessException;
import com.nasim.notification_service.shared.exception.ResourceNotFoundException;
import com.nasim.notification_service.shared.exception.TenantResolutionException;
import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.notification.event.NotificationQueuedEvent;
import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.repository.NotificationRepository;
import com.nasim.notification_service.repository.TemplateRepository;
import com.nasim.notification_service.routing.service.NotificationRouteService;
import com.nasim.notification_service.notification.service.NotificationService;
import com.nasim.notification_service.notification.service.NotificationStatusHistoryService;
import com.nasim.notification_service.routing.service.RoutingPolicyService;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NotificationServiceImpl implements NotificationService {

    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final RoutingPolicyService routingPolicyService;
    private final NotificationRouteService notificationRouteService;
    private final NotificationStatusHistoryService notificationStatusHistoryService;
    private final ApplicationEventPublisher eventPublisher;

    public NotificationServiceImpl(
            TemplateRepository templateRepository,
            NotificationRepository notificationRepository,
            RoutingPolicyService routingPolicyService, NotificationRouteService notificationRouteService,
            NotificationStatusHistoryService notificationStatusHistoryService, ApplicationEventPublisher eventPublisher
    ) {
        this.templateRepository = templateRepository;
        this.notificationRepository = notificationRepository;
        this.routingPolicyService = routingPolicyService;
        this.notificationRouteService = notificationRouteService;
        this.notificationStatusHistoryService = notificationStatusHistoryService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(
                ()-> new BusinessException("no_notification_found_by_id",notificationId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Notification create(@Valid NotificationDto request) {
        String tenantId = TenantContext.getTenantId();
        if (!StringUtils.hasText(tenantId)) {
            throw new TenantResolutionException("Tenant could not be resolved");
        }

        Template template = templateRepository.findByCodeAndActiveTrue(request.templateCode())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Template not found: " + request.templateCode()
                        )
                );

        RoutingPolicy routingPolicy = routingPolicyService.resolveRoutingPolicy(request.templateCode());

        Notification notification = new Notification();
        notification.setTenantID(tenantId);
        notification.setTemplate(template);
        notification.setRoutingPolicy(routingPolicy);
        notification.setRecipientAddress(request.recipientAddress());
        notification.setRecipientName(request.recipientName());
        notification.setSenderAddress(request.senderAddress());
        notification.setSenderName(request.senderName());
        notification.setPayloadJson(request.payloadJson());
        notification.setCurrentStatus(Notification.NotificationStatus.CREATED);

        Notification savedNotification = notificationRepository.save(notification);

        notificationStatusHistoryService.record(
                savedNotification,
                Notification.NotificationStatus.CREATED,
                "Notification created"
        );
        notificationRouteService.generateRoutingPlan(savedNotification, routingPolicy);


        savedNotification=this.updateNotificationStatus(savedNotification,
                Notification.NotificationStatus.QUEUED,"Notification queued for delivery",false);
        eventPublisher.publishEvent(new NotificationQueuedEvent(savedNotification.getId(), tenantId));
        return savedNotification;
    }

    @Override
    public List<Notification> listNotificationByStaus(Notification.NotificationStatus status) {
        return notificationRepository.findTop50ByCurrentStatusOrderByCreatedAtAsc(status);
    }

    @Override
    public Notification findbyIdAndStatus(Long notificationId, Notification.NotificationStatus notificationStatus) {
        return notificationRepository.findByIdAndStatus(notificationId, notificationStatus).orElseThrow(()
                -> new BusinessException("No_notification_found_by_id_status", notificationId, notificationStatus.name())
        );
    }

    @Override
    public Notification updateNotificationStatus(Notification notification, Notification.NotificationStatus status, String reason, Boolean fetch) {
        if(fetch || notification==null) notification=this.findById(notification.getId());
        notification.setCurrentStatus(status) ;
        Notification savedNotification = notificationRepository.save(notification);
        notificationStatusHistoryService.record(
                savedNotification,
                status,
                reason
        );
        return savedNotification;
    }
}