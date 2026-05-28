package com.hti.request;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationEntityRequest {
    private String organisationId;
    private String entityType;
    private Integer priority;
    private Map<String, Object> attributes;
}