package com.academins.academins.repositories;

import com.academins.academins.entities.AcademyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyClassRepository extends JpaRepository<AcademyClass, Long> {


}
