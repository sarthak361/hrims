package com.hti.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userresponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String organisationId;
    private String entityId;
    private LocalDateTime createdAt;
}