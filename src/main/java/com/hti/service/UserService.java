package com.hti.service;


import com.hti.request.Userrequest;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> create(Userrequest request);

    ResponseEntity<?> update(String id, Userrequest request);

    ResponseEntity<?> delete(String id);

    ResponseEntity<?> getById(String id);

    ResponseEntity<?> getAll(
            int page, int size,
            String sortBy, String sortDirection,
            String search
    );

    ResponseEntity<?> getByOrganisation(String organisationId);

    ResponseEntity<?> getByEntity(String entityId);
}