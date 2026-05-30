package com.hti.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hti.entity.organisation;

@Repository
public interface Organisationrepository extends JpaRepository<organisation, String> {
    Optional<organisation> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDomain(String domain);
    
    @Query("SELECT o FROM organisation o WHERE " +
    	       "(:search IS NULL OR " +
    	       "LOWER(o.organizationName) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
    	       "LOWER(o.organizationType) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
    	       "LOWER(o.registeredAddress) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
    	       "LOWER(o.email)             LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
    	       "LOWER(o.country)           LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR " +
    	       "LOWER(o.industryType)      LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
    	Page<organisation> searchOrganisations(
    	        @Param("search") String search,
    	        Pageable pageable
    	);
    	
}