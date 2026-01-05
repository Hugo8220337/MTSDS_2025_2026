package com.academins.academins.repositories;

import com.academins.academins.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Repository interface for School entity.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    /**
     * Find a School by its acronym.
     *
     * @param acronym The acronym of the School.
     * @return An Optional containing the School if found, otherwise empty.
     */
    Optional<School> findByAcronym(String acronym);

    /**
     * Check if a School exists by its acronym.
     *
     * @param acronym The acronym of the School.
     * @return true if a School with the given acronym exists, false otherwise.
     */
    boolean existsByAcronym(String acronym);

    /**
     * Delete a School by its acronym.
     *
     * @param acronym The acronym of the School to be deleted.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM School s WHERE s.acronym = :acronym")
    void deleteByAcronym(@Param("acronym") String acronym);

}
