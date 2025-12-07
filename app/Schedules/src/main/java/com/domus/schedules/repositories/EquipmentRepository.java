package com.domus.schedules.repositories;

import com.domus.schedules.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Equipment entity.
 * Equipment is part of the Classroom aggregate but has its own repository
 * as it can be managed independently for catalog purposes.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}

