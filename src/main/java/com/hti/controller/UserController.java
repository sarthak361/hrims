package com.hti.controller;

import com.hti.request.Userrequest;
import com.hti.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Userrequest request,
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

    @GetMapping("/entity/{entityId}")
    public ResponseEntity<?> getByEntity(@PathVariable String entityId,
                                          @RequestHeader String username,
                                          @RequestHeader String ipAddress) {
        return service.getByEntity(entityId, username, ipAddress);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                     @RequestBody Userrequest request,
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