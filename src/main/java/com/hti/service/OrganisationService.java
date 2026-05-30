package com.hti.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.hti.request.OrganisationRequest;

public interface OrganisationService {

	ResponseEntity<?> create(OrganisationRequest request);

	ResponseEntity<?> update(UUID id, OrganisationRequest request);

	ResponseEntity<?> delete(UUID id);

	ResponseEntity<?> getById(UUID id);

	ResponseEntity<?> getAll(
	        int page,
	        int size,
	        String sortBy,
	        String sortDirection,
	        String search,
	        String organizationType,
	        String industryType,
	        String city,
	        String state,
	        String country
	);
}
