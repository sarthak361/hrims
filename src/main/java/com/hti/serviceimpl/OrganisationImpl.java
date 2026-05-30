package com.hti.serviceimpl;

import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.hti.Repository.OrganisationRepository;
import com.hti.entity.organisation;
import com.hti.exception.BadRequestException;
import com.hti.exception.InternalServerException;
import com.hti.exception.NotFoundException;
import com.hti.request.OrganisationRequest;
import com.hti.response.OrganisationResponse;
import com.hti.response.PaginatedResponse;
import com.hti.service.OrganisationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganisationImpl implements OrganisationService {

	private static final Logger logger = LoggerFactory.getLogger("tracklogger");

	private final OrganisationRepository repository;

	@Override
	public ResponseEntity<?> create(OrganisationRequest request) {
		logger.info("Creating organisation | name={}", request.getOrganizationName());

		if (repository.existsByEmail(request.getEmail())) {
			logger.warn("Organisation already exists | email={}", request.getEmail());
			throw new BadRequestException("Organisation with email '" + request.getEmail() + "' already exists");
		}

		try {
			organisation org = organisation.builder().organizationName(request.getOrganizationName())
					.domain(request.getDomain()).organizationType(request.getOrganizationType())
					.companyRegistrationNumber(request.getCompanyRegistrationNumber())
					.websiteUrl(request.getWebsiteUrl()).logoUrl(request.getLogoUrl())
					.industryType(request.getIndustryType()).email(request.getEmail()).phone(request.getPhone())
					.registeredAddress(request.getRegisteredAddress()).city(request.getCity()).state(request.getState())
					.country(request.getCountry()).postalCode(request.getPostalCode()).timezone(request.getTimezone())
					.build();

			org = repository.save(org);
			logger.info("Organisation created successfully | id={} name={}", org.getId(), org.getOrganizationName());
			return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(org));

		} catch (BadRequestException ex) {
			throw ex;
		} catch (Exception ex) {
			logger.error("Error creating organisation | name={}", request.getOrganizationName(), ex);
			throw new InternalServerException("Failed to create organisation: " + ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> update(String id, OrganisationRequest request) {
		logger.info("Updating organisation | id={}", id);

		organisation org = repository.findById(id).orElseThrow(() -> {
			logger.warn("Organisation not found | id={}", id);
			return new NotFoundException("Organisation not found: " + id);
		});

		try {
			org.setOrganizationName(request.getOrganizationName());
			org.setDomain(request.getDomain());
			org.setOrganizationType(request.getOrganizationType());
			org.setCompanyRegistrationNumber(request.getCompanyRegistrationNumber());
			org.setWebsiteUrl(request.getWebsiteUrl());
			org.setLogoUrl(request.getLogoUrl());
			org.setIndustryType(request.getIndustryType());
			org.setEmail(request.getEmail());
			org.setPhone(request.getPhone());
			org.setRegisteredAddress(request.getRegisteredAddress());
			org.setCity(request.getCity());
			org.setState(request.getState());
			org.setCountry(request.getCountry());
			org.setPostalCode(request.getPostalCode());
			org.setTimezone(request.getTimezone());

			org = repository.save(org);
			logger.info("Organisation updated successfully | id={}", org.getId());
			return ResponseEntity.ok(toResponse(org));

		} catch (Exception ex) {
			logger.error("Error updating organisation | id={}", id, ex);
			throw new InternalServerException("Failed to update organisation: " + ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> delete(String id) {
		logger.info("Deleting organisation | id={}", id);

		if (!repository.existsById(id)) {
			logger.warn("Organisation not found | id={}", id);
			throw new NotFoundException("Organisation not found: " + id);
		}

		try {
			repository.deleteById(id);
			logger.info("Organisation deleted successfully | id={}", id);
			return ResponseEntity.ok("Organisation deleted successfully");

		} catch (Exception ex) {
			logger.error("Error deleting organisation | id={}", id, ex);
			throw new InternalServerException("Failed to delete organisation: " + ex.getMessage());
		}
	}

	@Override
	public ResponseEntity<?> getById(String id) {
		logger.info("Fetching organisation | id={}", id);

		organisation org = repository.findById(id).orElseThrow(() -> {
			logger.warn("Organisation not found | id={}", id);
			return new NotFoundException("Organisation not found: " + id);
		});

		logger.info("Organisation fetched successfully | id={}", id);
		return ResponseEntity.ok(toResponse(org));
	}

@Override
public ResponseEntity<?> getAll(int page, int size, String sortBy, String sortDirection,
                                 String search) {
    logger.info("Fetching organisations | page={}, size={}, sortBy={}, sortDir={}, search={}",
                page, size, sortBy, sortDirection, search);
    try {
       
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 5), 100);

        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(direction, sortField));

        Specification<organisation> spec = buildOrganisationSpec(search);

        Page<organisation> result = repository.findAll(spec, pageable);

        if (result.getTotalElements() == 0) {
            throw new NotFoundException("No Organisation found.");
        }

        var content = result.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        PaginatedResponse<OrganisationResponse> paginatedData = new PaginatedResponse<>(
                content,
                result.getNumber() + 1,   
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );

        logger.info("Organisations fetched successfully | totalElements={}", result.getTotalElements());
        return ResponseEntity.ok(paginatedData);

    } catch (NotFoundException ex) {
        throw ex;
    } catch (Exception ex) {
        logger.error("Error fetching organisations", ex);
        throw new InternalServerException("Failed to fetch organisations: " + ex.getMessage());
    }
}

private Specification<organisation> buildOrganisationSpec(String search) {
    return globalSearch(search); 
}

private Specification<organisation> globalSearch(String search) {
    return (root, query, cb) -> {
        if (search == null || search.trim().isEmpty()) return null; 
        String pattern = "%" + search.toLowerCase() + "%";
        return cb.or(
            cb.like(cb.lower(root.get("organizationName")),  pattern),
            cb.like(cb.lower(root.get("organizationType")),  pattern),
            cb.like(cb.lower(root.get("registeredAddress")), pattern),
            cb.like(cb.lower(root.get("email")),             pattern),
            cb.like(cb.lower(root.get("country")),           pattern),
            cb.like(cb.lower(root.get("industryType")),      pattern)
        );
    };
}

	private OrganisationResponse toResponse(organisation org) {
		return OrganisationResponse.builder().id(org.getId()).organizationName(org.getOrganizationName())
				.domain(org.getDomain()).organizationType(org.getOrganizationType())
				.companyRegistrationNumber(org.getCompanyRegistrationNumber()).websiteUrl(org.getWebsiteUrl())
				.logoUrl(org.getLogoUrl()).industryType(org.getIndustryType()).email(org.getEmail())
				.phone(org.getPhone()).registeredAddress(org.getRegisteredAddress()).city(org.getCity())
				.state(org.getState()).country(org.getCountry()).postalCode(org.getPostalCode())
				.timezone(org.getTimezone()).createdAt(org.getCreatedAt()).build();
	}
}