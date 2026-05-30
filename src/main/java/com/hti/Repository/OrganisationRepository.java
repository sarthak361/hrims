package com.hti.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hti.entity.organisation;

@Repository
public interface OrganisationRepository extends JpaRepository<organisation, String>,
JpaSpecificationExecutor<organisation> {  
    Optional<organisation> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDomain(String domain);    	
}