package com.codershubham.cms.cms.service.StudentManagementModules;


import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import com.codershubham.cms.cms.repository.StudentManagementModules.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

   @Autowired
   private CourseRepository courseRepository;

    // Create a new semester
    public SemesterModel createSemester(SemesterModel semester) {
        return semesterRepository.save(semester);
    }

    // Fetch all semesters
    public List<SemesterModel> findAll() {
        return semesterRepository.findAll();
    }

    public SemesterModel getSemesterById(Long semesterId) {
        // Fetch the Semester from the database using the repository
        return semesterRepository.findById(semesterId).orElse(null);  // Returns null if not found
    }

    public List<SemesterModel> getSemestersByCourse(Long courseId) {
        CourseModel course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return semesterRepository.findByCourse(course);
    }

    //    public List<Semester> getSemestersByCourse(Long courseId) {
//        return semesterRepository.findByCourse_Id(courseId);
//    }
//    public List<SemesterModel> getAllSemesters() {
//        return semesterRepository.findAll();
//    }
}
