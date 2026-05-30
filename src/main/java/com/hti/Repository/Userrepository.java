package com.hti.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hti.entity.User;

@Repository
public interface Userrepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByOrganisationId(String organisationId);
    List<User> findByEntityId(String entityId);
    @Query("SELECT u FROM User u WHERE " +
            "(:search IS NULL OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
            "LOWER(u.lastName)  LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
            "LOWER(u.email)     LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
            "LOWER(u.phone)     LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
     Page<User> searchUsers(
             @Param("search") String search,
             Pageable pageable
     );
}