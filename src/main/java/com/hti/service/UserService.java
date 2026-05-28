package com.hti.service;


import com.hti.request.Userrequest;

import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> create(Userrequest request, String username, String ipAddress);

    ResponseEntity<?> update(String id, Userrequest request, String username, String ipAddress);

    ResponseEntity<?> delete(String id, String username, String ipAddress);

    ResponseEntity<?> getById(String id, String username, String ipAddress);

    ResponseEntity<?> getAll(String username, String ipAddress);

    ResponseEntity<?> getByOrganisation(String organisationId, String username, String ipAddress);

    ResponseEntity<?> getByEntity(String entityId, String username, String ipAddress);
}