package com.domus.applications.repositories;

import com.domus.applications.entities.Application;
import com.domus.applications.entities.CourseOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseOptionRepository extends JpaRepository<CourseOption,Long> {
    List<CourseOption> findByApplication(Application application);

    @Query("SELECT co FROM CourseOption co WHERE co.id = :courseOptionId AND co.application = :application")
    Optional<CourseOption> findByIdAndApplication(Long courseOptionId, Application application);

    @Query("SELECT co FROM CourseOption co WHERE co.application.id = :id")
    List<CourseOption> findByApplicationId(Long id);
}
