package com.hti.serviceimpl;

import com.hti.request.OrganisationEntityRequest;
import org.springframework.data.domain.Pageable;
import com.hti.response.Organisationentityresponse;
import com.hti.response.PaginatedResponse;
import com.hti.entity.Organisationentity;
import com.hti.Repository.Organisationentityrepository;
import com.hti.service.OrganisationEntityService;
import com.hti.exception.NotFoundException;
import com.hti.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationEntityImpl implements OrganisationEntityService {

    //private static final Logger logger = LoggerFactory.getLogger(OrganisationEntityImpl.class);
	
	private static final Logger logger = LoggerFactory.getLogger("tracklogger");

	 

    private final Organisationentityrepository repository;

    @Override
    public ResponseEntity<?> create(OrganisationEntityRequest request) {
        logger.info("Creating entity | type={} orgId={}", request.getEntityType(), request.getOrganisationId());

        try {
            Organisationentity entity = Organisationentity.builder()
                    .organisationId(request.getOrganisationId())
                    .entityType(request.getEntityType())
                    .priority(request.getPriority())
                    .attributes(request.getAttributes())
                    .build();

            entity = repository.save(entity);
            logger.info("Entity created successfully | id={} type={}", entity.getId(), entity.getEntityType());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(entity));

        } catch (Exception ex) {
        	logger.error("Error creating entity | type={}", request.getEntityType(), ex);
            throw new InternalServerException("Failed to create entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(String id, OrganisationEntityRequest request) {
        logger.info("Updating entity | id={}", id);

        Organisationentity entity = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Entity not found | id={}", id);
                    return new NotFoundException("Entity not found: " + id);
                });

        try {
            entity.setEntityType(request.getEntityType());
            entity.setPriority(request.getPriority());
            entity.setAttributes(request.getAttributes());

            entity = repository.save(entity);
            logger.info("Entity updated successfully | id={}", entity.getId());
            return ResponseEntity.ok(toResponse(entity));

        } catch (Exception ex) {
        	logger.error("Error updating entity | id={}", id, ex);
            throw new InternalServerException("Failed to update entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("Deleting entity | id={}", id);

        if (!repository.existsById(id)) {
            logger.warn("Entity not found | id={}", id);
            throw new NotFoundException("Entity not found: " + id);
        }

        try {
            repository.deleteById(id);
            logger.info("Entity deleted successfully | id={}", id);
            return ResponseEntity.ok("Entity deleted successfully");

        } catch (Exception ex) {
        	logger.error("Error deleting entity | id={}", id, ex);
            throw new InternalServerException("Failed to delete entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("Fetching entity | id={}", id);

        Organisationentity entity = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Entity not found | id={}", id);
                    return new NotFoundException("Entity not found: " + id);
                });

        logger.info("Entity fetched successfully | id={}", id);
        return ResponseEntity.ok(toResponse(entity));
    }

    @Override
    public ResponseEntity<?> getAll(int page, int size, String sortBy, String sortDirection,
                                     String search) {
        logger.info("Fetching all entities | page={}, size={}, sortBy={}, sortDir={}, search={}",
                    page, size, sortBy, sortDirection, search);
        try {
            Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                    ? Sort.Direction.ASC : Sort.Direction.DESC;

            String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

            Page<Organisationentity> result = repository.searchEntities(
                    (search != null && !search.isBlank()) ? search : null,
                    pageable
            );

            if (result.isEmpty()) {
                throw new NotFoundException("No Organisation Entity found.");
            }

            var content = result.getContent()
                    .stream()
                    .map(this::toResponse)
                    .toList();

            PaginatedResponse<Organisationentityresponse> paginatedData = new PaginatedResponse<>(
                    content,
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages(),
                    result.isLast()
            );

            logger.info("Entities fetched successfully | totalElements={}", result.getTotalElements());
            return ResponseEntity.ok(paginatedData);

        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error fetching all entities", ex);
            throw new InternalServerException("Failed to fetch entities: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId) {
        logger.info("Fetching entities by org | orgId={}", organisationId);

        List<Organisationentityresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            logger.warn("No entities found | orgId={}", organisationId);
            throw new NotFoundException("No entities found for organisation: " + organisationId);
        }

        logger.info("Entities fetched successfully | orgId={} count={}", organisationId, list.size());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntityType(String entityType) {
        logger.info("Fetching entities by type | type={}", entityType);

        List<Organisationentityresponse> list = repository.findByEntityType(entityType)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            logger.warn("No entities found | type={}", entityType);
            throw new NotFoundException("No entities found for type: " + entityType);
        }

        logger.info("Entities fetched successfully | type={} count={}", entityType, list.size());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> searchByAttribute(String organisationId, String key, String value) {
        logger.info("Searching entity by attribute | orgId={} key={} value={}", organisationId, key, value);

        try {
            List<Organisationentityresponse> list = repository
                    .findByOrganisationIdAndAttribute(organisationId, key, value)
                    .stream().map(this::toResponse).collect(Collectors.toList());

            if (list.isEmpty()) {
                logger.warn("No entities found | key={} value={}", key, value);
                throw new NotFoundException("No entities found for key=" + key + " value=" + value);
            }

            logger.info("Entities found successfully | key={} value={} count={}", key, value, list.size());
            return ResponseEntity.ok(list);

        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
        	logger.error("Error searching entity | key={} value={}", key, value, ex);
            throw new InternalServerException("Failed to search entities: " + ex.getMessage());
        }
    }

    private Organisationentityresponse toResponse(Organisationentity entity) {
        return Organisationentityresponse.builder()
                .id(entity.getId())
                .organisationId(entity.getOrganisationId())
                .entityType(entity.getEntityType())
                .priority(entity.getPriority())
                .attributes(entity.getAttributes())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}