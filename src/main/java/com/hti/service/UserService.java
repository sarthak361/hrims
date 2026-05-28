package com.hti.service;


import com.hti.request.Userrequest;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> create(Userrequest request);

    ResponseEntity<?> update(String id, Userrequest request);

    ResponseEntity<?> delete(String id);

    ResponseEntity<?> getById(String id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> getByOrganisation(String organisationId);

    ResponseEntity<?> getByEntity(String entityId);
}