package com.codershubham.cms.cms.service.StudentManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.*;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentEnrollmentRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.SubjectEnrollmentRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RoleRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final RoleRepository roleRepository;
    private final SubjectEnrollmentRepository subjectEnrollmentRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public StudentService(UserRepository userRepository, RoleRepository roleRepository, CourseRepository courseRepository, StudentRepository studentRepository, SubjectEnrollmentRepository subjectEnrollmentRepository, StudentEnrollmentRepository studentEnrollmentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.subjectEnrollmentRepository = subjectEnrollmentRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.roleRepository = roleRepository;
        this.courseRepository = courseRepository;
        // Password encoder instance
    }

    @Transactional
    public StudentModel registerStudent(String username, String password, String firstName, String lastName, String email, String phoneNumber, String address, CourseModel course) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (studentRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Find the Role by its name
        RoleModel role = roleRepository.findByName("STUDENT").orElseThrow(() -> new RuntimeException("Role not found"));
        System.out.println("Assigned Role: " + role.getName());

        // Hash the password (Use BCryptPasswordEncoder)
//        String hashedPassword = passwordEncoder.encode(password);
//
        // Create User object
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user = userRepository.save(user); // Save user to the database

        // Verify course exists before assigning
        CourseModel assignedCourse = courseRepository.findById(course.getCourseID()).orElseThrow(() -> new RuntimeException("Course not found"));

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

    public void saveStudentsFromExcel(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String username = row.getCell(0).getStringCellValue();
                String password = row.getCell(1).getStringCellValue();
                String roleName = row.getCell(2).getStringCellValue();
                String firstName = row.getCell(3).getStringCellValue();
                String lastName = row.getCell(4).getStringCellValue();
                String email = row.getCell(5).getStringCellValue();
                String phoneNumber = row.getCell(6).getStringCellValue();
                String address = row.getCell(7).getStringCellValue();
                Long courseId = (long) row.getCell(8).getNumericCellValue();

                if (userRepository.existsByUsername(username)) {
                    throw new RuntimeException("Username already exists");
                }
                if (studentRepository.existsByEmail(email)) {
                    throw new RuntimeException("Email already exists");
                }

                // Create new student
                UserModel user = new UserModel();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));

                RoleModel role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
                user.setRole(role);
                userRepository.save(user);

                CourseModel course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

                StudentModel student = new StudentModel();
                student.setUser(user);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setEmail(email);
                student.setPhoneNumber(phoneNumber);
                student.setAddress(address);
                student.setCourse(course);
                studentRepository.save(student);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }
    }

    public List<StudentModel> getStudentsByDivision(Long divisionId) {
        List<StudentEnrollmentModel> enrollments = studentEnrollmentRepository.findByDivisionId(divisionId);
        return enrollments.stream().map(StudentEnrollmentModel::getStudent).toList();
    }

    public List<StudentModel> getUnassignedStudentsBySemester(Long semesterId) {
        return studentRepository.findUnassignedStudentsBySemester(semesterId);
    }

    public StudentModel findById(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
    }

    public StudentModel getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID " + userId)); // Return null if no student is found
    }

    public List<StudentEnrollmentModel> getEnrollmentsByStudentId(Long studentId) {
        return studentEnrollmentRepository.findByStudentId(studentId);
    }

    public List<SubjectEnrollmentModel> getSubjectsByStudentIdAndSemester(Long studentId, Long semesterId) {
        return subjectEnrollmentRepository.findByStudentIdAndSemesterId(studentId, semesterId);
    }
}
