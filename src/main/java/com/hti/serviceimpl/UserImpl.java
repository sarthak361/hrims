package com.hti.serviceimpl;

import com.hti.request.Userrequest;
import com.hti.response.Userresponse;
import com.hti.entity.User;
import com.hti.Repository.Userrepository;
import com.hti.service.UserService;
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

        if (repository.existsByEmail(request.getEmail())) {
            logger.warn("User already exists | email={}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with email '" + request.getEmail() + "' already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(request.getRole())
                .organisationId(request.getOrganisationId())
                .entityId(request.getEntityId())
                .build();

        user = repository.save(user);
        logger.info("User created | id={} email={}", user.getId(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }

    @Override
    public ResponseEntity<?> update(String id, Userrequest request) {
        logger.info("Updating user | id={}", id);

        User user = repository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + id);
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setEntityId(request.getEntityId());

        user = repository.save(user);
        logger.info("User updated | id={}", user.getId());
        return ResponseEntity.ok(toResponse(user));
    }

    @Override
    public ResponseEntity<?> delete(String id) {
        logger.info("Deleting user | id={}", id);

        if (!repository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + id);
        }

        repository.deleteById(id);
        logger.info("User deleted | id={}", id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Override
    public ResponseEntity<?> getById(String id) {
        logger.info("Fetching user | id={}", id);

        User user = repository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + id);
        }
        return ResponseEntity.ok(toResponse(user));
    }

    @Override
    public ResponseEntity<?> getAll() {
        logger.info("Fetching all users");
        List<Userresponse> list = repository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByOrganisation(String organisationId) {
        logger.info("Fetching users by org | orgId={}", organisationId);
        List<Userresponse> list = repository.findByOrganisationId(organisationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<?> getByEntity(String entityId) {
        logger.info("Fetching users by entity | entityId={}", entityId);
        List<Userresponse> list = repository.findByEntityId(entityId)
                .stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    private Userresponse toResponse(User user) {
        return Userresponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .organisationId(user.getOrganisationId())
                .entityId(user.getEntityId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}