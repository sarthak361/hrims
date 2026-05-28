package com.hti.service;
 
import com.hti.request.Organisationrequest;
import org.springframework.http.ResponseEntity;
 
public interface OrganisationService {
 
    ResponseEntity<?> create(Organisationrequest request);
 
    ResponseEntity<?> update(String id, Organisationrequest request);
 
    ResponseEntity<?> delete(String id);
 
    ResponseEntity<?> getById(String id);
 
    ResponseEntity<?> getAll();
}
 