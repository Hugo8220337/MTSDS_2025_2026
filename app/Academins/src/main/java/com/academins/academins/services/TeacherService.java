package com.academins.academins.services;

import com.academins.academins.dto.request.AssignCurricularUnitsToTeacherRequestDto;
import com.academins.academins.dto.request.EnrollStudentsInClassRequestDto;
import com.academins.academins.dto.request.TeacherRequestDTO;
import com.academins.academins.dto.response.TeacherResponseDTO;
import com.academins.academins.entities.*;
import com.academins.academins.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    private final ModelMapper mapper;
    private final TeacherRepository teacherRepository;
    private final TeacherCURepository teacherCURepository;
    private final StudentRepository studentRepository;
    private final StudentClassCURepository studentClassCURepository;
    private final CurricularUnitsRepository curricularUnitsRepository;
    private final AcademyClassRepository academyClassRepository;
    private final SchoolYearRepository schoolYearRepository;

    public TeacherService(ModelMapper mapper, TeacherRepository teacherRepository, TeacherCURepository teacherCURepository, StudentRepository studentRepository, StudentClassCURepository studentClassCURepository, CurricularUnitsRepository curricularUnitsRepository, AcademyClassRepository academyClassRepository, SchoolYearRepository schoolYearRepository) {
        this.mapper = mapper;
        this.teacherRepository = teacherRepository;
        this.teacherCURepository = teacherCURepository;
        this.studentRepository = studentRepository;
        this.studentClassCURepository = studentClassCURepository;
        this.curricularUnitsRepository = curricularUnitsRepository;
        this.academyClassRepository = academyClassRepository;
        this.schoolYearRepository = schoolYearRepository;
    }

    /**
     * Create a new Teacher
     * @param request Data Transfer Object containing details of the Teacher to be created
     * @return The created Teacher DTO
     */
    public Teacher createTeacher(TeacherRequestDTO request) {
        Teacher teacher = mapper.map(request, Teacher.class);
        return teacherRepository.save(teacher);
    }

    /**
     * Get all Teachers
     * @return List of Teacher DTOs
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Get a Teacher by its ID
     * @param id The ID of the Teacher
     * @return An Optional containing the Teacher DTO if found, otherwise empty
     */
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Teacher with ID " + id + " does not exist.")
        );
    }

    /**
     * Update an existing Teacher
     * @param id The ID of the Teacher to be updated
     * @param teacherResponseDTO Data Transfer Object containing updated details of the Teacher
     * @return The updated Teacher DTO
     * @throws IllegalArgumentException if the Teacher with the given ID does not exist
     */
    public Teacher updateTeacher(Long id, TeacherResponseDTO teacherResponseDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher with ID " + id + " does not exist."));

        mapper.map(teacherResponseDTO, existingTeacher);
        return teacherRepository.save(existingTeacher);
    }

    /**
     * Delete a Teacher by its ID
     * @param id The ID of the Teacher to be deleted
     * @throws IllegalArgumentException if the Teacher with the given ID does not exist
     */
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new IllegalArgumentException("Teacher with ID " + id + " does not exist.");
        }
        teacherRepository.deleteById(id);
    }

    /**
     * Get all Students enrolled in a specific Curricular Unit under a Teacher
     * @param teacherId The ID of the Teacher
     * @param codeCU The code of the Curricular Unit
     * @return List of Students enrolled in the specified Curricular Unit
     */
    public List<Student> getStudentsInCurricularUnit(Long teacherId, String codeCU) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + teacherId + " does not exist."));

        CurricularUnit curricularUnit = curricularUnitsRepository.findByCode(codeCU)
                .orElseThrow(() -> new EntityNotFoundException("Curricular Unit with code " + codeCU + " does not exist."));

        return studentClassCURepository.getByCurricularUnit(curricularUnit).stream()
                .map(StudentClassCU::getStudent)
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsInCurricularUnit(Long teacherId, String codeCU, String schoolYear) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + teacherId + " does not exist."));

        CurricularUnit curricularUnit = curricularUnitsRepository.findByCode(codeCU)
                .orElseThrow(() -> new EntityNotFoundException("Curricular Unit with code " + codeCU + " does not exist."));

        SchoolYear yearEntity = schoolYearRepository.findByYear(schoolYear);
        if (yearEntity == null) {
            throw new EntityNotFoundException("School Year " + schoolYear + " does not exist.");
        }

        return studentClassCURepository.findByCurricularUnitAndSchoolYear(curricularUnit, yearEntity).stream()
                .map(StudentClassCU::getStudent)
                .collect(Collectors.toList());
    }

    /**
     * Enroll multiple students in a specific Curricular Unit under a Teacher
     * @param teacherId The ID of the Teacher
     * @param codeCU The code of the Curricular Unit
     * @param enrollRequest Data Transfer Object containing details of the enrollment
     * @return List of StudentClassCU entities representing the enrollments
     */
    @Transactional
    public List<StudentClassCU> enrollStudentsInCurricularUnit(Long teacherId, String codeCU, EnrollStudentsInClassRequestDto enrollRequest) {
        // Check if Teacher exists
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + teacherId + " does not exist."));

        // Check if Curricular Unit exists
        CurricularUnit curricularUnit = curricularUnitsRepository.findByCode(codeCU)
                .orElseThrow(() -> new EntityNotFoundException("Curricular Unit with code " + codeCU + " does not exist."));

        // Check if School Year exists
        SchoolYear schoolYear = schoolYearRepository.findById(enrollRequest.getSchoolYearId())
                .orElseThrow(() -> new EntityNotFoundException("School Year with ID " + enrollRequest.getSchoolYearId() + " does not exist."));

        // Check if Academy Class exists
        AcademyClass academyClass = academyClassRepository.findById(enrollRequest.getAcademyClassId())
                .orElseThrow(() -> new EntityNotFoundException("Academy Class with ID " + enrollRequest.getAcademyClassId() + " does not exist."));

        List<StudentClassCU> studentsToEnroll = new ArrayList<>();
        for(Long studentId : enrollRequest.getStudentIds()) {
            // Check if student exists
            Student student = studentRepository.findById(studentId).orElseThrow(
                    () -> new EntityNotFoundException("Student with ID " + studentId + " does not exist.")
            );

            StudentClassCU studentToEnroll = new StudentClassCU(schoolYear, student, academyClass, curricularUnit);
            studentsToEnroll.add(studentToEnroll);
        }
        return studentClassCURepository.saveAll(studentsToEnroll);
    }

    @Transactional
    public List<TeacherCU> assignCurricularUnitsToTeacher(Integer teacherNumber, AssignCurricularUnitsToTeacherRequestDto curricularUnitIds) {
        // Check if Teacher exists
        SchoolYear schoolYear = schoolYearRepository.findById(curricularUnitIds.getSchoolYearId())
                .orElseThrow(() -> new EntityNotFoundException("School Year with ID " + curricularUnitIds.getSchoolYearId() + " does not exist."));

        Teacher teacher = teacherRepository.findByWorkerNumber(teacherNumber)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with worker number " + teacherNumber + " does not exist."));

        List<TeacherCU> assignedTeacherCUs = new ArrayList<>();
        for(Long cuId : curricularUnitIds.getCurricularUnitIds()) {
            // Check if Curricular Unit exists
            CurricularUnit curricularUnit = curricularUnitsRepository.findById(cuId)
                    .orElseThrow(() -> new EntityNotFoundException("Curricular Unit with ID " + cuId + " does not exist."));

            // Create TeacherCU association
            TeacherCU teacherCU = new TeacherCU(teacher, curricularUnit, schoolYear);
            assignedTeacherCUs.add(teacherCU);
        }

        return teacherCURepository.saveAll(assignedTeacherCUs);
    }
}
