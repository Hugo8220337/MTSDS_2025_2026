package com.academins.academins.services;

import com.academins.academins.dto.request.AcademyClassRequestDTO;
import com.academins.academins.dto.request.StudentsToEnrollRequestDto;
import com.academins.academins.dto.response.AcademyClassResponseDTO;
import com.academins.academins.entities.*;
import com.academins.academins.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcademyClassService {

    private final ModelMapper modelMapper;
    private final AcademyClassRepository academyClassRepository;
    private final StudentRepository studentRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final CurricularUnitsRepository curricularUnitsRepository;
    private final StudentClassCURepository studentClassCURepository;


    @Autowired
    public AcademyClassService(ModelMapper modelMapper, AcademyClassRepository academyClassRepository, StudentRepository studentRepository, SchoolYearRepository schoolYearRepository, CurricularUnitsRepository curricularUnitsRepository, StudentClassCURepository studentClassCURepository) {
        this.modelMapper = modelMapper;
        this.academyClassRepository = academyClassRepository;
        this.studentRepository = studentRepository;
        this.schoolYearRepository = schoolYearRepository;
        this.curricularUnitsRepository = curricularUnitsRepository;
        this.studentClassCURepository = studentClassCURepository;
    }

    /**
     * Create a new AcademyClass
     *
     * @param request Data Transfer Object containing details of the AcademyClass to be created
     * @return The created AcademyClass DTO
     */
    public AcademyClass createAcademyClass(AcademyClassRequestDTO request) {
        SchoolYear schoolYear = schoolYearRepository.findById(request.getSchoolYearId()).orElseThrow(
                () -> new EntityNotFoundException("SchoolYear with ID " + request.getSchoolYearId() + " does not exist.")
        );

        AcademyClass academyClass = modelMapper.map(request, AcademyClass.class);
        academyClass.setSchoolYear(schoolYear);

        return academyClassRepository.save(academyClass);
    }

    /**
     * Get all AcademyClasses
     *
     * @return List of AcademyClass DTOs
     */
    public List<AcademyClass> getAllAcademyClasses() {
        return academyClassRepository.findAll();
    }

    /**
     * Get an AcademyClass by its ID
     *
     * @param id The ID of the AcademyClass
     * @return An Optional containing the AcademyClass DTO if found, otherwise empty
     */
    public AcademyClass getAcademyClassById(Long id) {
        return academyClassRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("AcademyClass with ID " + id + " does not exist.")
        );
    }

    /**
     * Update an existing AcademyClass
     *
     * @param id                      The ID of the AcademyClass to be updated
     * @param academyClassResponseDTO Data Transfer Object containing updated details of the AcademyClass
     * @return The updated AcademyClass DTO
     * @throws IllegalArgumentException if the AcademyClass with the given ID does not exist
     */
    public AcademyClass updateAcademyClass(Long id, AcademyClassResponseDTO academyClassResponseDTO) {
        AcademyClass existingAcademyClass = academyClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("AcademyClass with ID " + id + " does not exist."));

        modelMapper.map(academyClassResponseDTO, existingAcademyClass);
        return academyClassRepository.save(existingAcademyClass);
    }

    /**
     * Delete an AcademyClass by its ID
     *
     * @param id The ID of the AcademyClass to be deleted
     * @throws IllegalArgumentException if the AcademyClass with the given ID does not exist
     */
    public void deleteAcademyClass(Long id) {
        if (!academyClassRepository.existsById(id)) {
            throw new IllegalArgumentException("AcademyClass with ID " + id + " does not exist.");
        }
        academyClassRepository.deleteById(id);
    }

    /**
     * Enroll multiple students in a specific class
     *
     * @param classId    The ID of the class
     * @param requestDTO Data Transfer Object containing the list of student IDs to be enrolled
     * @throws IllegalArgumentException if the class or any student is not found, or if class capacity is exceeded
     */
    @Transactional
    public AcademyClass enrollStudentsInClasses(Long classId, StudentsToEnrollRequestDto requestDTO) {
        // find SchoolYear
        SchoolYear schoolYear = schoolYearRepository.findById(requestDTO.getSchoolYearId()).orElseThrow(
                () -> new EntityNotFoundException("SchoolYear with ID " + requestDTO.getSchoolYearId() + " does not exist.")
        );

        // find curricular unit
        CurricularUnit curricularUnit = curricularUnitsRepository.findById(requestDTO.getCurricularUnitId()).orElseThrow(
                () -> new EntityNotFoundException("CurricularUnit with ID " + requestDTO.getCurricularUnitId() + " does not exist.")
        );

        // find academy class
        AcademyClass academyClass = academyClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class with ID " + classId + " not found"));

        // Get all Students by their IDs
        List<Long> studentIds = requestDTO.getStudentsIds();
        List<Student> studentsToEnroll = studentRepository.findAllById(studentIds);

        // Verify if all students were found
        if (studentsToEnroll.size() != studentIds.size()) {
            throw new IllegalArgumentException("One or more students not found");
        }

        // Verify class capacity
        int currentStudents = academyClass.getStudents().size();
        if (currentStudents + studentsToEnroll.size() > academyClass.getMaxVacancies()) {
            throw new IllegalArgumentException("Class capacity exceeded");
        }

        List<StudentClassCU> assignments = new ArrayList<>();
        for (Student student : studentsToEnroll) {
            StudentClassCU assignment = new StudentClassCU(
                    schoolYear,
                    student,
                    academyClass,
                    curricularUnit
            );
            assignments.add(assignment);
        }
        studentClassCURepository.saveAll(assignments);

        return academyClassRepository.findById(classId).orElse(academyClass);
    }

    /**
     * Get all students enrolled in a specific class
     *
     * @param classId The ID of the class
     * @return List of Student DTOs enrolled in the class
     */
    public List<Student> getStudentsInClass(Long classId) {
        AcademyClass academyClass = academyClassRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Class with ID " + classId + " not found"));

        List<StudentClassCU> studentsClassCU = studentClassCURepository.findByAcademyClass(academyClass);
        List<Student> students = new ArrayList<>();
        for (StudentClassCU scu : studentsClassCU) {
            students.add(scu.getStudent());
        }

        return students;
    }
}
