package com.academins.academins.repositories;

import com.academins.academins.entities.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {

    SchoolYear findByYear(String year);
}
