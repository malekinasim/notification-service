package com.nasim.notification_service.model.entity;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.tool.schema.extract.internal.ColumnInformationImpl;
import org.hibernate.tool.schema.extract.spi.ColumnInformation;
import org.hibernate.tool.schema.extract.spi.ColumnTypeInformation;

@Getter
@Setter
@Entity
@Table(name = "provider",indexes = {
        @Index(name = "idx_provider_type",columnList = "type")
},uniqueConstraints = {
        @UniqueConstraint(name = "uk_provider_type", columnNames = "type")
})
public class Provider extends BaseEntity<Long> {
     @Enumerated(EnumType.STRING)
     @Column(name="type",nullable = false,length = 50,unique = true)
     private ProviderType type;
     @Column(name="name",nullable = false,length = 200)
     private String name;
     @Column(name="description",length = 1000)
     private String description;
     @Column(name="config_metadata",nullable = false,columnDefinition = "TEXT")
     private String configMetadata;

     public enum ProviderType{
          SMTP,
          SENDGRID,
          TWILIO
     }

}
