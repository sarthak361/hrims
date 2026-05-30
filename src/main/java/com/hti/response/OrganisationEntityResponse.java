package com.hti.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationEntityResponse {
    private String id;
    private String organisationId;
    private String entityType;
    private Integer priority;
    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
}