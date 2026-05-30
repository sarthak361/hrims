package com.hti.controller;

import com.hti.request.UserRequest;
import com.hti.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest request) {
        return service.create(request);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)    String sortBy,
            @RequestParam(required = false)    String sortDirection,
            @RequestParam(required = false)    String search
    ) {
        return service.getAll(page, size, sortBy, sortDirection, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/org/{organisationId}")
    public ResponseEntity<?> getByOrganisation(@PathVariable String organisationId) {
        return service.getByOrganisation(organisationId);
    }

    @GetMapping("/entity/{entityId}")
    public ResponseEntity<?> getByEntity(@PathVariable String entityId) {
        return service.getByEntity(entityId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable String id,
                                    @Valid @RequestBody UserRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return service.delete(id);
    }
}