package com.hti.service;

import com.hti.request.OrganisationEntityRequest;
import org.springframework.http.ResponseEntity;

public interface OrganisationEntityService {

    ResponseEntity<?> create(OrganisationEntityRequest request);

    ResponseEntity<?> update(String id, OrganisationEntityRequest request);

    ResponseEntity<?> delete(String id);

    ResponseEntity<?> getById(String id);

    ResponseEntity<?> getAll(
            int page, int size,
            String sortBy, String sortDirection,
            String search
    );

    ResponseEntity<?> getByOrganisation(String organisationId);

    ResponseEntity<?> getByEntityType(String entityType);

    ResponseEntity<?> searchByAttribute(String organisationId, String key, String value);
}