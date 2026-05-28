package com.hti.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organisationrequest {
    private String organizationName;
    private String domain;
    private String organizationType;
    private String companyRegistrationNumber;
    private String websiteUrl;
    private String logoUrl;
    private String industryType;
    private String email;
    private String phone;
    private String registeredAddress;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String timezone;
}