package com.hti.Repository;

import com.hti.entity.Organisationentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Organisationentityrepository extends JpaRepository<Organisationentity, String> {

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