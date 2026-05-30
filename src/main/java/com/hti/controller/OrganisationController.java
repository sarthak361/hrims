package com.hti.controller;

import com.hti.request.OrganisationRequest;
import com.hti.service.OrganisationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organisations")
@RequiredArgsConstructor
public class OrganisationController {

	private final OrganisationService service;

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody OrganisationRequest request) {
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

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody OrganisationRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		return service.delete(id);
	}
}