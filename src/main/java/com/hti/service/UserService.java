package com.hti.service;


import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.hti.request.UserRequest;

public interface UserService {

    ResponseEntity<?> create(UserRequest request);

    ResponseEntity<?> update(UUID id, UserRequest request);

    ResponseEntity<?> delete(UUID id);

    ResponseEntity<?> getById(UUID id);

    ResponseEntity<?> getAll(
            int page,
            int size,
            String sortBy,
            String sortDirection,
            String search,
            UUID organisationId,
            UUID entityId
    );

    ResponseEntity<?> getByOrganisation(UUID organisationId);

    ResponseEntity<?> getByEntity(UUID entityId);
}