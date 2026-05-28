package com.hti.service;
 
import com.hti.request.Organisationrequest;
import org.springframework.http.ResponseEntity;
 
public interface OrganisationService {
 
    ResponseEntity<?> create(Organisationrequest request, String username, String ipAddress);
 
    ResponseEntity<?> update(String id, Organisationrequest request, String username, String ipAddress);
 
    ResponseEntity<?> delete(String id, String username, String ipAddress);
 
    ResponseEntity<?> getById(String id, String username, String ipAddress);
 
    ResponseEntity<?> getAll(String username, String ipAddress);
}
 