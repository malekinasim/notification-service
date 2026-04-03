package com.nasim.notification_service.repository;

import com.nasim.notification_service.model.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByCodeAndActiveTrue(String code);
}