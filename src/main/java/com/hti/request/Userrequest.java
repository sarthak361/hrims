package com.hti.request;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userrequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String organisationId;
    private String entityId;
    private Map<String, Object> attributes;  // dynamic JSONB fields
}