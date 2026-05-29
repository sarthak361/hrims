package com.hti.serviceimpl;

import com.hti.request.OrganisationEntityRequest;
import com.hti.response.Organisationentityresponse;
import com.hti.entity.Organisationentity;
import com.hti.Repository.Organisationentityrepository;
import com.hti.service.OrganisationEntityService;
import com.hti.exception.NotFoundException;
import com.hti.exception.BadRequestException;
import com.hti.exception.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganisationEntityImpl implements OrganisationEntityService {

    private static final Logger logger = LoggerFactory.getLogger(OrganisationEntityImpl.class);

    private final Organisationentityrepository repository;

    @Override
    public ResponseEntity<?> create(OrganisationEntityRequest request) {
        logger.info("Creating entity | type={} orgId={}", request.getEntityType(), request.getOrganisationId());

        if (request.getOrganisationId() == null || request.getOrganisationId().isBlank()) {
            throw new BadRequestException("organisationId is required");
        }

        if (request.getEntityType() == null || request.getEntityType().isBlank()) {
            throw new BadRequestException("entityType is required");
        }

        try {
            Organisationentity entity = Organisationentity.builder()
                    .organisationId(request.getOrganisationId())
                    .entityType(request.getEntityType())
                    .priority(request.getPriority())
                    .attributes(request.getAttributes())
                    .build();

            entity = repository.save(entity);
            logger.info("Entity created | id={} type={}", entity.getId(), entity.getEntityType());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(entity));

        } catch (Exception ex) {
            logger.error("Error creating entity | type={} error={}", request.getEntityType(), ex.getMessage());
            throw new InternalServerException("Failed to create entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(String id, OrganisationEntityRequest request) {
        logger.info("Updating entity | id={}", id);

        Organisationentity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found: " + id));

        if (request.getEntityType() == null || request.getEntityType().isBlank()) {
            throw new BadRequestException("entityType is required");
        }

        try {
            entity.setEntityType(request.getEntityType());
            entity.setPriority(request.getPriority());
            entity.setAttributes(request.getAttributes());

            entity = repository.save(entity);
            logger.info("Entity updated | id={}", entity.getId());
            return ResponseEntity.ok(toResponse(entity));

        } catch (Exception ex) {
            logger.error("Error updating entity | id={} error={}", id, ex.getMessage());
            throw new InternalServerException("Failed to update entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("Deleting entity | id={}", id);

        if (!repository.existsById(id)) {
            throw new NotFoundException("Entity not found: " + id);
        }

        try {
            repository.deleteById(id);
            logger.info("Entity deleted | id={}", id);
            return ResponseEntity.ok("Entity deleted successfully");

        } catch (Exception ex) {
            logger.error("Error deleting entity | id={} error={}", id, ex.getMessage());
            throw new InternalServerException("Failed to delete entity: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("Fetching entity | id={}", id);

        Organisationentity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity not found: " + id));

        return ResponseEntity.ok(toResponse(entity));
    }

    @Override
    public ResponseEntity<?> getAll() {
        logger.info("Fetching all entities");

        try {
            List<Organisationentityresponse> list = repository.findAll()
                    .stream().map(this::toResponse).collect(Collectors.toList());
            return ResponseEntity.ok(list);

        } catch (Exception ex) {
            logger.error("Error fetching all entities | error={}", ex.getMessage());
            throw new InternalServerException("Failed to fetch entities: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId) {
        logger.info("Fetching entities by org | orgId={}", organisationId);

        if (organisationId == null || organisationId.isBlank()) {
            throw new BadRequestException("organisationId is required");
        }

        List<Organisationentityresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("No entities found for organisation: " + organisationId);
        }

        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntityType(String entityType) {
        logger.info("Fetching entities by type | type={}", entityType);

        if (entityType == null || entityType.isBlank()) {
            throw new BadRequestException("entityType is required");
        }

        List<Organisationentityresponse> list = repository.findByEntityType(entityType)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("No entities found for type: " + entityType);
        }

        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> searchByAttribute(String organisationId, String key, String value) {
        logger.info("Searching entity by attribute | orgId={} key={} value={}", organisationId, key, value);

        if (organisationId == null || organisationId.isBlank()) {
            throw new BadRequestException("organisationId is required");
        }

        if (key == null || key.isBlank()) {
            throw new BadRequestException("search key is required");
        }

        if (value == null || value.isBlank()) {
            throw new BadRequestException("search value is required");
        }

        try {
            List<Organisationentityresponse> list = repository
                    .findByOrganisationIdAndAttribute(organisationId, key, value)
                    .stream().map(this::toResponse).collect(Collectors.toList());

            if (list.isEmpty()) {
                throw new NotFoundException("No entities found for key=" + key + " value=" + value);
            }

            return ResponseEntity.ok(list);

        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error searching entity | key={} value={} error={}", key, value, ex.getMessage());
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