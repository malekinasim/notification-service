package com.nasim.notification_service.service.impl;

import com.nasim.notification_service.config.tenant.TenantContext;
import com.nasim.notification_service.exception.ResourceNotFoundException;
import com.nasim.notification_service.exception.TenantResolutionException;
import com.nasim.notification_service.model.dto.NotificationDto;
import com.nasim.notification_service.model.entity.Notification;
import com.nasim.notification_service.model.entity.RoutingPolicy;
import com.nasim.notification_service.model.entity.Template;
import com.nasim.notification_service.repository.NotificationRepository;
import com.nasim.notification_service.repository.TemplateRepository;
import com.nasim.notification_service.service.NotificationRoutService;
import com.nasim.notification_service.service.NotificationService;
import com.nasim.notification_service.service.NotificationStatusHistoryService;
import com.nasim.notification_service.service.RoutingPolicyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(rollbackFor = Exception.class)
public class NotificationServiceImpl implements NotificationService {

    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final RoutingPolicyService routingPolicyService;
    private final NotificationRoutService notificationRoutService;
    private final NotificationStatusHistoryService notificationStatusHistoryService;

    public NotificationServiceImpl(
            TemplateRepository templateRepository,
            NotificationRepository notificationRepository,
            RoutingPolicyService routingPolicyService, NotificationRoutService notificationRoutService,
            NotificationStatusHistoryService notificationStatusHistoryService
    ) {
        this.templateRepository = templateRepository;
        this.notificationRepository = notificationRepository;
        this.routingPolicyService = routingPolicyService;
        this.notificationRoutService = notificationRoutService;
        this.notificationStatusHistoryService = notificationStatusHistoryService;
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
        notificationRoutService.generateRoutingPlan(savedNotification,routingPolicy);

        savedNotification.setCurrentStatus(Notification.NotificationStatus.QUEUED);

        savedNotification = notificationRepository.save(savedNotification);
        notificationStatusHistoryService.record(
                savedNotification,
                Notification.NotificationStatus.QUEUED,
                "Notification queued for delivery"
        );

        return savedNotification;
    }
}