package com.hti.service;

import org.springframework.http.ResponseEntity;

import com.hti.request.Organisationrequest;

public interface OrganisationService {

	ResponseEntity<?> create(Organisationrequest request);

	ResponseEntity<?> update(String id, Organisationrequest request);

	ResponseEntity<?> delete(String id);

	ResponseEntity<?> getById(String id);

	ResponseEntity<?> getAll(
	        int page, int size,
	        String sortBy, String sortDirection,
	        String search
	);
}
