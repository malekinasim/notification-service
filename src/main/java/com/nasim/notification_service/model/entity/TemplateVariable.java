package com.nasim.notification_service.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@Table(
        name = "template_variable",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_template_variable_name",
                        columnNames = {"template_id", "name"}
                )
        }
)
public class TemplateVariable extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "required", nullable = false)
    private boolean required = false;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 30)
    private TemplateVariableDataType dataType;

    @Column(name = "default_value", columnDefinition = "TEXT")
    private String defaultValue;

    public enum TemplateVariableDataType {
        STRING,
        NUMBER,
        BOOLEAN,
        DATE,
        JSON
    }
}
