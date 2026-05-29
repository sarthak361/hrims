package com.hti.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganisationEntityRequest {

    @NotBlank(message = "Organisation ID is required")
    private String organisationId;

    @NotBlank(message = "Entity type is required")
    private String entityType;

    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 100, message = "Priority must be at most 100")
    private Integer priority;

    private Map<String, Object> attributes;
}