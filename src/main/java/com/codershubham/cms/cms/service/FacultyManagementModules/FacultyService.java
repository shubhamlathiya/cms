package com.codershubham.cms.cms.service.FacultyManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.DepartmentModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.LeaveManagementModules.LeaveRequestModel;
import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.model.UserManagementModules.RoleModel;
import com.codershubham.cms.cms.model.UserManagementModules.UserModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.DepartmentRepository;
import com.codershubham.cms.cms.repository.FacultyManagementModules.FacultyRepository;
import com.codershubham.cms.cms.repository.LeaveManagementModules.LeaveRequestRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.DivisionRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.RoleRepository;
import com.codershubham.cms.cms.repository.UserManagementModules.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    public List<FacultyModel> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Optional<FacultyModel> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public FacultyModel findById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Faculty not found with ID: " + id));
    }

    @Transactional
    public FacultyModel registerFaculty(String username, String password, String firstName, String lastName, String designation, String qualification,
                                        int experience, String phoneNumber, String email, DepartmentModel department, String status) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists (You can also check for faculty-specific email uniqueness)
        if (facultyRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Find the Role by its name (Assuming you have a ROLE_FACULTY in your roles table)
        RoleModel role = roleRepository.findByName("FACULTY")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        System.out.println("Assigned Role: " + role.getName());

        // Hash the password (Use BCryptPasswordEncoder or similar)
//        String hashedPassword = passwordEncoder.encode(password);

        // Create User object
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));  // Ensure password is hashed
        user.setRole(role);
        user = userRepository.save(user); // Save user to the database

        // Verify the department exists before assigning
        DepartmentModel assignedDepartment = departmentRepository.findById(department.getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Create Faculty object
        FacultyModel faculty = new FacultyModel();
        faculty.setUser(user);
        faculty.setFirstName(firstName);
        faculty.setLastName(lastName);
        faculty.setDesignation(designation);
        faculty.setQualification(qualification);
        faculty.setExperience(experience);
        faculty.setPhoneNumber(phoneNumber);
        faculty.setEmail(email);
        faculty.setDepartment(assignedDepartment);
        faculty.setCreatedAt(LocalDateTime.now());
        faculty.setUpdatedAt(LocalDateTime.now());// Set timestamp
        faculty.setStatus(status);// Assign the department to the faculty

        return facultyRepository.save(faculty); // Save the faculty to the database
    }

    public FacultyModel updateFaculty(Long id, FacultyModel facultyDetails) {
        FacultyModel faculty = facultyRepository.findById(id).orElseThrow();
        faculty.setDesignation(facultyDetails.getDesignation());
        faculty.setQualification(facultyDetails.getQualification());
        faculty.setExperience(facultyDetails.getExperience());
        faculty.setPhoneNumber(facultyDetails.getPhoneNumber());
        faculty.setEmail(facultyDetails.getEmail());
        faculty.setStatus(facultyDetails.getStatus());
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<FacultyModel> getFacultyByDepartment(Long departmentId) {
        return facultyRepository.findByDepartmentId(departmentId);
    }

    public FacultyModel getFacultyByUserId(Long userId) {
        return facultyRepository.findByUserId(userId)
                .orElse(null); // Return null if no faculty member is found
    }

    public List<DivisionModel> getDivisionsByFacultyId(Long facultyId) {
        return divisionRepository.findByFacultyId(facultyId);
    }
//    public List<FacultyModel> getFacultiesByDepartmentId(Long departmentId) {
//        return facultyRepository.findByDepartmentId(departmentId);
//    }
}
