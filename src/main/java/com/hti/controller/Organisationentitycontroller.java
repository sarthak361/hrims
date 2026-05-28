package com.hti.controller;

import com.hti.request.OrganisationEntityRequest;
import com.hti.service.OrganisationEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entities")
@RequiredArgsConstructor
public class Organisationentitycontroller {

    private final OrganisationEntityService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody OrganisationEntityRequest request,
                                     @RequestHeader String username,
                                     @RequestHeader String ipAddress) {
        return service.create(request, username, ipAddress);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestHeader String username,
                                     @RequestHeader String ipAddress) {
        return service.getAll(username, ipAddress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id,
                                      @RequestHeader String username,
                                      @RequestHeader String ipAddress) {
        return service.getById(id, username, ipAddress);
    }

    @GetMapping("/org/{organisationId}")
    public ResponseEntity<?> getByOrganisation(@PathVariable String organisationId,
                                                @RequestHeader String username,
                                                @RequestHeader String ipAddress) {
        return service.getByOrganisation(organisationId, username, ipAddress);
    }

    @GetMapping("/type/{entityType}")
    public ResponseEntity<?> getByEntityType(@PathVariable String entityType,
                                              @RequestHeader String username,
                                              @RequestHeader String ipAddress) {
        return service.getByEntityType(entityType, username, ipAddress);
    }

    @GetMapping("/org/{organisationId}/search")
    public ResponseEntity<?> searchByAttribute(@PathVariable String organisationId,
                                                @RequestParam String key,
                                                @RequestParam String value,
                                                @RequestHeader String username,
                                                @RequestHeader String ipAddress) {
        return service.searchByAttribute(organisationId, key, value, username, ipAddress);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                     @RequestBody OrganisationEntityRequest request,
                                     @RequestHeader String username,
                                     @RequestHeader String ipAddress) {
        return service.update(id, request, username, ipAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id,
                                     @RequestHeader String username,
                                     @RequestHeader String ipAddress) {
        return service.delete(id, username, ipAddress);
    }
}