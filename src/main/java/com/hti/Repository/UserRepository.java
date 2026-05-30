package com.hti.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hti.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>,
JpaSpecificationExecutor<User>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByOrganisationId(String organisationId);
    List<User> findByEntityId(String entityId);
}