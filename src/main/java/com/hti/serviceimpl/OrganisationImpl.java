package com.hti.serviceimpl;

import com.hti.request.Organisationrequest;
import com.hti.response.Organisationresponse;
import com.hti.entity.organisation;
import com.hti.Repository.Organisationrepository;
import com.hti.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationImpl implements OrganisationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganisationImpl.class);

    private final Organisationrepository repository;

    @Override
    public ResponseEntity<?> create(Organisationrequest request) {
        logger.info("{}: Creating organisation | name={}",  request.getOrganizationName());

        if (repository.existsByEmail(request.getEmail())) {
            logger.warn("{}: Organisation already exists | email={}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Organisation with email '" + request.getEmail() + "' already exists");
        }

        organisation org = organisation.builder()
                .organizationName(request.getOrganizationName())
                .domain(request.getDomain())
                .organizationType(request.getOrganizationType())
                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
                .websiteUrl(request.getWebsiteUrl())
                .logoUrl(request.getLogoUrl())
                .industryType(request.getIndustryType())
                .email(request.getEmail())
                .phone(request.getPhone())
                .registeredAddress(request.getRegisteredAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .timezone(request.getTimezone())
                .build();

        org = repository.save(org);
        logger.info("{}: Organisation created | id={} name={}", org.getId(), org.getOrganizationName());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(org));
    }

    @Override
    public ResponseEntity<?> update(String id, Organisationrequest request) {
        logger.info("{}: Updating organisation | id={}", id);

        organisation org = repository.findById(id).orElse(null);
        if (org == null) {
            logger.warn("{}: Organisation not found | id={}",  id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organisation not found: " + id);
        }

        org.setOrganizationName(request.getOrganizationName());
        org.setDomain(request.getDomain());
        org.setOrganizationType(request.getOrganizationType());
        org.setCompanyRegistrationNumber(request.getCompanyRegistrationNumber());
        org.setWebsiteUrl(request.getWebsiteUrl());
        org.setLogoUrl(request.getLogoUrl());
        org.setIndustryType(request.getIndustryType());
        org.setEmail(request.getEmail());
        org.setPhone(request.getPhone());
        org.setRegisteredAddress(request.getRegisteredAddress());
        org.setCity(request.getCity());
        org.setState(request.getState());
        org.setCountry(request.getCountry());
        org.setPostalCode(request.getPostalCode());
        org.setTimezone(request.getTimezone());

        org = repository.save(org);
        logger.info("{}: Organisation updated | id={}",org.getId());
        return ResponseEntity.ok(toResponse(org));
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("{}: Deleting organisation | id={}",  id);

        if (!repository.existsById(id)) {
            logger.warn("{}: Organisation not found | id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organisation not found: " + id);
        }

        repository.deleteById(id);
        logger.info("{}: Organisation deleted | id={}",  id);
        return ResponseEntity.ok("Organisation deleted successfully");
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("{}: Fetching organisation | id={}",  id);

        organisation org = repository.findById(id).orElse(null);
        if (org == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organisation not found: " + id);
        }
        return ResponseEntity.ok(toResponse(org));
    }

    @Override
    public ResponseEntity<?> getAll() {
        logger.info("{}: Fetching all organisations");
        List<Organisationresponse> list = repository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    private Organisationresponse toResponse(organisation org) {
        return Organisationresponse.builder()
                .id(org.getId())
                .organizationName(org.getOrganizationName())
                .domain(org.getDomain())
                .organizationType(org.getOrganizationType())
                .companyRegistrationNumber(org.getCompanyRegistrationNumber())
                .websiteUrl(org.getWebsiteUrl())
                .logoUrl(org.getLogoUrl())
                .industryType(org.getIndustryType())
                .email(org.getEmail())
                .phone(org.getPhone())
                .registeredAddress(org.getRegisteredAddress())
                .city(org.getCity())
                .state(org.getState())
                .country(org.getCountry())
                .postalCode(org.getPostalCode())
                .timezone(org.getTimezone())
                .createdAt(org.getCreatedAt())
                .build();
    }
}