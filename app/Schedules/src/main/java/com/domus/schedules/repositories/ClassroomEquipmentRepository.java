package com.domus.schedules.repositories;

import com.domus.schedules.entities.ClassroomEquipment;
import com.domus.schedules.entities.Classroom;
import com.domus.schedules.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ClassroomEquipment entity.
 * Manages the association between classrooms and equipment.
 */
@Repository
public interface ClassroomEquipmentRepository extends JpaRepository<ClassroomEquipment, Long> {

    @Query("SELECT ce FROM ClassroomEquipment ce WHERE ce.classroom.id = :classroomId AND ce.equipment.id = :equipmentId")
    ClassroomEquipment findByClassroomIdAndEquipmentId(Long classroomId, Long equipmentId);

    @Query("SELECT ce.equipment FROM ClassroomEquipment ce WHERE ce.classroom.id = :classroomId")
    List<Equipment> findByClassroomId(Long classroomId);
}

