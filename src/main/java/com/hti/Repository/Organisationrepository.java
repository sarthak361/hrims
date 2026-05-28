package com.hti.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hti.entity.organisation;

@Repository
public interface Organisationrepository extends JpaRepository<organisation, String> {
    Optional<organisation> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDomain(String domain);
}