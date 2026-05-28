package com.hti.service;

import com.hti.request.OrganisationEntityRequest;
import org.springframework.http.ResponseEntity;

public interface OrganisationEntityService {

    ResponseEntity<?> create(OrganisationEntityRequest request, String username, String ipAddress);

    ResponseEntity<?> update(String id, OrganisationEntityRequest request, String username, String ipAddress);

    ResponseEntity<?> delete(String id, String username, String ipAddress);

    ResponseEntity<?> getById(String id, String username, String ipAddress);

    ResponseEntity<?> getAll(String username, String ipAddress);

    ResponseEntity<?> getByOrganisation(String organisationId, String username, String ipAddress);

    ResponseEntity<?> getByEntityType(String entityType, String username, String ipAddress);

    ResponseEntity<?> searchByAttribute(String organisationId, String key, String value, String username, String ipAddress);
}