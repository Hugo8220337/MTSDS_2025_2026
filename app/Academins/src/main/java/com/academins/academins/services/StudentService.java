package com.academins.academins.services;

import com.academins.academins.dto.request.StudentRequestDTO;
import com.academins.academins.dto.response.CurricularUnitResponseDTO;
import com.academins.academins.entities.Course;
import com.academins.academins.entities.CurricularUnit;
import com.academins.academins.entities.Student;
import com.academins.academins.entities.StudentClassCU;
import com.academins.academins.repositories.CourseRepository;
import com.academins.academins.repositories.StudentClassCURepository;
import com.academins.academins.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentClassCURepository studentClassCURepository;


    public StudentService(ModelMapper modelMapper, StudentRepository studentRepository, CourseRepository courseRepository, StudentClassCURepository studentClassCURepository) {
        this.modelMapper = modelMapper;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.studentClassCURepository = studentClassCURepository;
    }

    /**
     * Create a new Student (self-register)
     * @param studentDTO Data Transfer Object containing details of the Student to be created
     * @return The created Student DTO
     * @throws IllegalArgumentException if the provided studentDTO is null
     */
    public Student createStudent(StudentRequestDTO studentDTO) {
        if (studentDTO == null) {
            throw new IllegalArgumentException("Student data cannot be null");
        }

        Student student = modelMapper.map(studentDTO, Student.class);

        // Map course if courseCode is provided
        if (studentDTO.getCourseCode() != null) {
            Course course = courseRepository.findByCourseCode(studentDTO.getCourseCode())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Course with Code " + studentDTO.getCourseCode() + " does not exist."
                    ));
            student.setCourse(course);
        }

        // Generate unique student number
        student.setStudentNumber(generateStudentNumber());

        return studentRepository.save(student);
    }



    /**
     * Generate a unique student number
     * @return A unique student number as a String
     */
    private String generateStudentNumber() {
        // Example: current year + 5 digit sequential number
        String year = String.valueOf(java.time.Year.now().getValue());
        Long count = studentRepository.count() + 1;
        return year + String.format("%05d", count); // Ex: 2025000001
    }


    /**
     * Get all Students
     * @return List of Student DTOs
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    /**
     * Update an existing Student's name, email, and telephone number
     * @param studentNumber The number of the Student to be updated
     * @param studentDTO Data Transfer Object containing updated details of the Student
     * @return The updated Student DTO
     * @throws IllegalArgumentException if the Student with the given ID does not exist
     */
    public Student updateStudent(String studentNumber,StudentRequestDTO studentDTO) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student number " + studentNumber + " does not exist."
                ));

        //Check if immutable data is being changed
        if (studentDataCheck(student, studentDTO)) {
            throw new IllegalArgumentException("Cannot change immutable student data (student number or course).");
        }

        // Update only name, email, and telephone number
        if (studentDTO.getFullName() != null) {
            student.setFullName(studentDTO.getFullName());
        }
        if (studentDTO.getEmail() != null) {
            student.setEmail(studentDTO.getEmail());
        }
        if (studentDTO.getTelephoneNumber() != null) {
            student.setTelephoneNumber(studentDTO.getTelephoneNumber());
        }

        return studentRepository.save(student);
    }

    /**
     * Check if immutable student data (student number, fin, date of birth) is being changed
     * @param student The existing Student entity
     * @param studentDTO The StudentDTO containing updated data
     * @return true if immutable data is being changed, false otherwise
     */
    private boolean studentDataCheck(Student student, StudentRequestDTO studentDTO) {
        // Check if student number is being changed
        if (studentDTO.getStudentNumber() != null &&
                !studentDTO.getStudentNumber().equals(student.getStudentNumber())) {
            return true;
        }

        // Check if FIN is being changed
        if (studentDTO.getFin() != null &&
                !studentDTO.getFin().equals(student.getFin())) {
            return true;
        }

        // Check if date of birth is being changed
        if (studentDTO.getDateOfBirth() != null &&
                !studentDTO.getDateOfBirth().equals(student.getDateOfBirth())) {
            return true;
        }

        // Check if course is being changed
        if (studentDTO.getCourseCode() != null) {
            String existingCourseCode = student.getCourse() != null
                    ? student.getCourse().getCourseCode()
                    : null;
            return !studentDTO.getCourseCode().equals(existingCourseCode);
        }

        return false;
    }
    
    /**
     * Delete a Student by its ID
     * @param id The ID of the Student to be deleted
     * @throws IllegalArgumentException if the Student with the given ID does not exist
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " does not exist.");
        }
        studentRepository.deleteById(id);
    }

    public Student getStudentByStudentNumber(String studentNumber) {
        return studentRepository.findByStudentNumber(studentNumber).orElseThrow(
                () -> new EntityNotFoundException("Student with number " + studentNumber + " does not exist.")
        );
    }

    public List<CurricularUnit> getCurricularUnitsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("Student with ID " + studentId + " does not exist.")
        );

        List<StudentClassCU> studentClassCus = studentClassCURepository.findByStudent(student);
        return studentClassCus.stream()
                .map(StudentClassCU::getCurricularUnit)
                .collect(Collectors.toList());
    }

    public Student getStudentByStudentId(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(
                () -> new EntityNotFoundException("Student with ID " + studentId + " does not exist.")
        );
    }
}
