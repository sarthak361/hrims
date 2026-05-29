package com.hti.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organisationrequest {

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    private String organizationName;

    @NotBlank(message = "Domain is required")
    private String domain;

    private String organizationType;

    private String companyRegistrationNumber;

    private String websiteUrl;

    private String logoUrl;

    private String industryType;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 digits")
    private String phone;

    private String registeredAddress;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private String timezone;
}