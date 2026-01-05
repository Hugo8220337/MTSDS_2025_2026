package com.academins.academins.repositories;

import com.academins.academins.entities.AcademyClass;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.entities.SchoolYear;
import com.academins.academins.entities.Student;
import com.academins.academins.entities.StudentClassCU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentClassCURepository extends JpaRepository<StudentClassCU, Long> {

    List<StudentClassCU> findByAcademyClass(AcademyClass academyClass);

    List<StudentClassCU> findByStudent(Student student);

    List<StudentClassCU> getByCurricularUnit(CurricularUnit curricularUnit);
    
    List<StudentClassCU> findByCurricularUnitAndSchoolYear(CurricularUnit curricularUnit, SchoolYear schoolYear);
}
