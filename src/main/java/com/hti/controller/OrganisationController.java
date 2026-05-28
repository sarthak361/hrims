package com.hti.controller;

import com.hti.request.Organisationrequest;
import com.hti.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organisations")
@RequiredArgsConstructor
public class OrganisationController {

    private final OrganisationService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Organisationrequest request,
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

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                     @RequestBody Organisationrequest request,
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