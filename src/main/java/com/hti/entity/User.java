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
@Table(name = "users")
public class User {
	 
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;
 
    @Column(name = "first_name", nullable = false)
    private String firstName;
 
    @Column(name = "last_name", nullable = false)
    private String lastName;
 
    @Column(name = "email", nullable = false, unique = true)
    private String email;
 
    @Column(name = "phone")
    private String phone;
 
    @Column(name = "password", nullable = false)
    private String password;
 
    @Column(name = "role")
    private String role;
 
    // Plain String fields — no ManyToOne mapping
    @Column(name = "organisation_id", nullable = false)
    private String organisationId;
 
    @Column(name = "entity_id")
    private String entityId;
 
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Map<String, Object> attributes;
 
    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}