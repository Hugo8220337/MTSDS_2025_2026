package com.academins.academins.repositories;

import com.academins.academins.entities.TeacherCU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherCURepository extends JpaRepository<TeacherCU, Long> {

}
