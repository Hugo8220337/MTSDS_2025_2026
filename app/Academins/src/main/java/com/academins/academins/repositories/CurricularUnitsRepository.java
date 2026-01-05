package com.academins.academins.repositories;


import com.academins.academins.entities.CurricularUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for CurricularUnit entity.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface CurricularUnitsRepository extends JpaRepository<CurricularUnit, Long> {

    /** Check if a Curricular Unit exists by its codeCU.
     *
     * @param codeCU The code of the Curricular Unit.
     * @return true if a Curricular Unit with the given codeCU exists, false otherwise.
     */

    boolean existsByCodeCU(String codeCU);

    /** Check if a Curricular Unit exists by its name.
     *
     * @param name The name of the Curricular Unit.
     * @return true if a Curricular Unit with the given name exists, false otherwise.
     */
    boolean existsByName(String name);


    @Query("SELECT cu FROM CurricularUnit cu WHERE cu.codeCU = :codeCU")
    Optional<CurricularUnit> findByCode(String codeCU);
}
