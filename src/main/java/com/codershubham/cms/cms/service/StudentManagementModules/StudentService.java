package com.codershubham.cms.cms.service.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentEnrollmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentEnrollmentRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RoleRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;

    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(UserRepository userRepository, RoleRepository roleRepository, CourseRepository courseRepository, StudentRepository studentRepository, StudentEnrollmentRepository studentEnrollmentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        // Password encoder instance
    }

    @Transactional
    public StudentModel registerStudent(String username, String password, String firstName, String lastName,
                                        String email, String phoneNumber, String address, CourseModel course) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (studentRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Find the Role by its name
        RoleModel role = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        System.out.println("Assigned Role: " + role.getName());

        // Hash the password (Use BCryptPasswordEncoder)
        String hashedPassword = passwordEncoder.encode(password);
//
        // Create User object
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user = userRepository.save(user); // Save user to the database

        // Verify course exists before assigning
        CourseModel assignedCourse = courseRepository.findById(course.getCourseID())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Create Student object
        StudentModel student = new StudentModel();
        student.setUser(user);  // Link the student to the user
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setAddress(address);
        student.setCourse(assignedCourse);  // Assign the course to the student

        return studentRepository.save(student); // Save the student to the database
    }

//    public List<StudentModel> getAllStudents() {
//        return studentRepository.findAll();
//    }
//
//    public List<StudentModel> findStudentsByCourse(Long courseId) {
//        CourseModel course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        return studentRepository.findByCourse(course);
////        return studentRepository.findByCourseId(courseId);
//    }
//
//
//    public List<StudentModel> getStudentsByCourseId(Long courseId) {
//        CourseModel course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        return studentRepository.findByCourse(course);  // Pass Course object
//    }

    public List<StudentModel> getStudentsByDivision(Long divisionId) {
        List<StudentEnrollmentModel> enrollments = studentEnrollmentRepository.findByDivisionId(divisionId);
        return enrollments.stream().map(StudentEnrollmentModel::getStudent).toList();
    }

//    public List<StudentModel> getUnassignedStudents() {
//        return studentRepository.findUnassignedStudents();
//    }

    public List<StudentModel> getUnassignedStudentsBySemester(Long semesterId) {
        return studentRepository.findUnassignedStudentsBySemester(semesterId);
    }

    public StudentModel findById(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
    }


}
