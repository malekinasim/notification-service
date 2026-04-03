package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "template",uniqueConstraints = {@UniqueConstraint(name = "template_code_uk",
        columnNames = "code"
)})
public class Template extends BaseEntity<Long>{

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, length = 30)
    private TemplateContentType contentType = TemplateContentType.TEXT;

    public enum TemplateContentType{
        TEXT,HTML,MARKDOWN,JSON
    }
}
