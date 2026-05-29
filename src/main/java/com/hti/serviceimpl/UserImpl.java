package com.hti.serviceimpl;

import com.hti.request.Userrequest;
import com.hti.response.Userresponse;
import com.hti.entity.User;
import com.hti.Repository.Userrepository;
import com.hti.service.UserService;
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
public class UserImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    private final Userrepository repository;

    @Override
    public ResponseEntity<?> create(Userrequest request) {
        logger.info("Creating user | email={}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("email is required");
        }

        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new BadRequestException("firstName is required");
        }

        if (request.getOrganisationId() == null || request.getOrganisationId().isBlank()) {
            throw new BadRequestException("organisationId is required");
        }

        if (repository.existsByEmail(request.getEmail())) {
            logger.warn("User already exists | email={}", request.getEmail());
            throw new BadRequestException("User with email '" + request.getEmail() + "' already exists");
        }

        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .password(request.getPassword())
                    .organisationId(request.getOrganisationId())
                    .entityId(request.getEntityId())
                    .build();

            user = repository.save(user);
            logger.info("User created | id={} email={}", user.getId(), user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));

        } catch (Exception ex) {
            logger.error("Error creating user | email={} error={}", request.getEmail(), ex.getMessage());
            throw new InternalServerException("Failed to create user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(String id, Userrequest request) {
        logger.info("Updating user | id={}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new BadRequestException("firstName is required");
        }

        try {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());
            user.setEntityId(request.getEntityId());

            user = repository.save(user);
            logger.info("User updated | id={}", user.getId());
            return ResponseEntity.ok(toResponse(user));

        } catch (Exception ex) {
            logger.error("Error updating user | id={} error={}", id, ex.getMessage());
            throw new InternalServerException("Failed to update user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("Deleting user | id={}", id);

        if (!repository.existsById(id)) {
            throw new NotFoundException("User not found: " + id);
        }

        try {
            repository.deleteById(id);
            logger.info("User deleted | id={}", id);
            return ResponseEntity.ok("User deleted successfully");

        } catch (Exception ex) {
            logger.error("Error deleting user | id={} error={}", id, ex.getMessage());
            throw new InternalServerException("Failed to delete user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("Fetching user | id={}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));

        return ResponseEntity.ok(toResponse(user));
    }

    @Override
    public ResponseEntity<?> getAll() {
        logger.info("Fetching all users");

        try {
            List<Userresponse> list = repository.findAll()
                    .stream().map(this::toResponse).collect(Collectors.toList());
            return ResponseEntity.ok(list);

        } catch (Exception ex) {
            logger.error("Error fetching all users | error={}", ex.getMessage());
            throw new InternalServerException("Failed to fetch users: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId) {
        logger.info("Fetching users by org | orgId={}", organisationId);

        if (organisationId == null || organisationId.isBlank()) {
            throw new BadRequestException("organisationId is required");
        }

        List<Userresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("No users found for organisation: " + organisationId);
        }

        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntity(String entityId) {
        logger.info("Fetching users by entity | entityId={}", entityId);

        if (entityId == null || entityId.isBlank()) {
            throw new BadRequestException("entityId is required");
        }

        List<Userresponse> list = repository.findByEntityId(entityId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new NotFoundException("No users found for entity: " + entityId);
        }

        return ResponseEntity.ok(list);
    }

    private Userresponse toResponse(User user) {
        return Userresponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .organisationId(user.getOrganisationId())
                .entityId(user.getEntityId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}