package com.hti.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hti.Repository.Userrepository;
import com.hti.entity.User;
import com.hti.exception.BadRequestException;
import com.hti.exception.InternalServerException;
import com.hti.exception.NotFoundException;
import com.hti.request.Userrequest;
import com.hti.response.PaginatedResponse;
import com.hti.response.Userresponse;
import com.hti.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

    private final Userrepository repository;

    @Override
    public ResponseEntity<?> create(Userrequest request) {
        logger.info("Creating user | email={}", request.getEmail());

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
            logger.info("User created successfully | id={} email={}", user.getId(), user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));

        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
        	logger.error("Error creating user | email={}", request.getEmail(), ex);
            throw new InternalServerException("Failed to create user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(String id, Userrequest request) {
        logger.info("Updating user | id={}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found | id={}", id);
                    return new NotFoundException("User not found: " + id);
                });

        try {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());
            user.setEntityId(request.getEntityId());

            user = repository.save(user);
            logger.info("User updated successfully | id={}", user.getId());
            return ResponseEntity.ok(toResponse(user));

        } catch (Exception ex) {
        	logger.error("Error updating user | id={}", id, ex);
            throw new InternalServerException("Failed to update user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("Deleting user | id={}", id);

        if (!repository.existsById(id)) {
            logger.warn("User not found | id={}", id);
            throw new NotFoundException("User not found: " + id);
        }

        try {
            repository.deleteById(id);
            logger.info("User deleted successfully | id={}", id);
            return ResponseEntity.ok("User deleted successfully");

        } catch (Exception ex) {
        	logger.error("Error deleting user | id={}", id, ex);
            throw new InternalServerException("Failed to delete user: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("Fetching user | id={}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found | id={}", id);
                    return new NotFoundException("User not found: " + id);
                });

        logger.info("User fetched successfully | id={}", id);
        return ResponseEntity.ok(toResponse(user));
    }

    @Override
    public ResponseEntity<?> getAll(int page, int size, String sortBy, String sortDirection,
                                     String search) {
        logger.info("Fetching all users | page={}, size={}, sortBy={}, sortDir={}, search={}",
                    page, size, sortBy, sortDirection, search);
        try {
            Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                    ? Sort.Direction.ASC : Sort.Direction.DESC;

            String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

            Page<User> result = repository.searchUsers(
                    (search != null && !search.isBlank()) ? search : null,
                    pageable
            );

            if (result.isEmpty()) {
                throw new NotFoundException("No User found.");
            }

            var content = result.getContent()
                    .stream()
                    .map(this::toResponse)
                    .toList();

            PaginatedResponse<Userresponse> paginatedData = new PaginatedResponse<>(
                    content,
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages(),
                    result.isLast()
            );

            logger.info("Users fetched successfully | totalElements={}", result.getTotalElements());
            return ResponseEntity.ok(paginatedData);

        } catch (NotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error fetching all users", ex);
            throw new InternalServerException("Failed to fetch users: " + ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId) {
        logger.info("Fetching users by org | orgId={}", organisationId);

        List<Userresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            logger.warn("No users found | orgId={}", organisationId);
            throw new NotFoundException("No users found for organisation: " + organisationId);
        }

        logger.info("Users fetched successfully | orgId={} count={}", organisationId, list.size());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntity(String entityId) {
        logger.info("Fetching users by entity | entityId={}", entityId);

        List<Userresponse> list = repository.findByEntityId(entityId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        if (list.isEmpty()) {
            logger.warn("No users found | entityId={}", entityId);
            throw new NotFoundException("No users found for entity: " + entityId);
        }

        logger.info("Users fetched successfully | entityId={} count={}", entityId, list.size());
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