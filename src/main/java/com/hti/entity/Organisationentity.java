package com.hti.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "organisation_entity")
public class Organisationentity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "organisation_id", nullable = false)
    private String organisationId;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "priority")
    private Integer priority;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}