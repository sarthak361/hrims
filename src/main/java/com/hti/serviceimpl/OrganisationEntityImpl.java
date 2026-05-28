package com.hti.serviceimpl;


import com.hti.request.OrganisationEntityRequest;
import com.hti.response.Organisationentityresponse;
import com.hti.entity.Organisationentity;
import com.hti.Repository.Organisationentityrepository;
import com.hti.service.OrganisationEntityService;
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
    public ResponseEntity<?> create(OrganisationEntityRequest request, String username, String ipAddress) {
        logger.info("{}: Creating entity | type={} orgId={}", username, request.getEntityType(), request.getOrganisationId());

        Organisationentity entity = Organisationentity.builder()
                .organisationId(request.getOrganisationId())
                .entityType(request.getEntityType())
                .priority(request.getPriority())
                .attributes(request.getAttributes())
                .build();

        entity = repository.save(entity);
        logger.info("{}: Entity created | id={} type={}", username, entity.getId(), entity.getEntityType());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(entity));
    }

    @Override
    public ResponseEntity<?> update(String id, OrganisationEntityRequest request, String username, String ipAddress) {
        logger.info("{}: Updating entity | id={}", username, id);

        Organisationentity entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + id);
        }

        entity.setEntityType(request.getEntityType());
        entity.setPriority(request.getPriority());
        entity.setAttributes(request.getAttributes());

        entity = repository.save(entity);
        logger.info("{}: Entity updated | id={}", username, entity.getId());
        return ResponseEntity.ok(toResponse(entity));
    }

    @Override
    public ResponseEntity<?> delete(String id, String username, String ipAddress) {
        logger.info("{}: Deleting entity | id={}", username, id);

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + id);
        }

        repository.deleteById(id);
        logger.info("{}: Entity deleted | id={}", username, id);
        return ResponseEntity.ok("Entity deleted successfully");
    }

    @Override
    public ResponseEntity<?> getById(String id, String username, String ipAddress) {
        logger.info("{}: Fetching entity | id={}", username, id);

        Organisationentity entity = repository.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + id);
        }
        return ResponseEntity.ok(toResponse(entity));
    }

    @Override
    public ResponseEntity<?> getAll(String username, String ipAddress) {
        logger.info("{}: Fetching all entities", username);
        List<Organisationentityresponse> list = repository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId, String username, String ipAddress) {
        logger.info("{}: Fetching entities by org | orgId={}", username, organisationId);
        List<Organisationentityresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntityType(String entityType, String username, String ipAddress) {
        logger.info("{}: Fetching entities by type | type={}", username, entityType);
        List<Organisationentityresponse> list = repository.findByEntityType(entityType)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> searchByAttribute(String organisationId, String key, String value, String username, String ipAddress) {
        logger.info("{}: Searching entity by attribute | orgId={} key={} value={}", username, organisationId, key, value);
        List<Organisationentityresponse> list = repository.findByOrganisationIdAndAttribute(organisationId, key, value)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
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