package com.hti.service;

import org.springframework.http.ResponseEntity;

import com.hti.request.OrganisationRequest;

public interface OrganisationService {

	ResponseEntity<?> create(OrganisationRequest request);

	ResponseEntity<?> update(String id, OrganisationRequest request);

	ResponseEntity<?> delete(String id);

	ResponseEntity<?> getById(String id);

	ResponseEntity<?> getAll(
	        int page, int size,
	        String sortBy, String sortDirection,
	        String search
	);
}
