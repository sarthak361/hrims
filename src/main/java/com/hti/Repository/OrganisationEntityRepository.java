package com.hti.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hti.entity.Organisationentity;

@Repository
public interface OrganisationEntityRepository extends JpaRepository<Organisationentity, String>,
JpaSpecificationExecutor<Organisationentity>{

    List<Organisationentity> findByOrganisationId(String organisationId);

    List<Organisationentity> findByEntityType(String entityType);

    List<Organisationentity> findByOrganisationIdAndEntityType(String organisationId, String entityType);

    // JSONB attribute search
    @Query(value = """
        SELECT * FROM organisation_entity
        WHERE organisation_id = :orgId
        AND attributes ->> :key = :value
        """, nativeQuery = true)
    List<Organisationentity> findByOrganisationIdAndAttribute(
        @Param("orgId") String orgId,
        @Param("key") String key,
        @Param("value") String value
    );
    
}