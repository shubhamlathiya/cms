package com.codershubham.cms.cms.service.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public CourseModel createCourse(CourseModel courseModel) {
        courseModel.setCreatedAt(LocalDateTime.now());
        return courseRepository.save(courseModel);
    }

    public List<CourseModel> getAllCourses() {
        return courseRepository.findAll();
    }

    public CourseModel updateCourse(Long courseID, CourseModel updatedCourseModel) {
        CourseModel courseModel = courseRepository.findById(courseID).orElseThrow(() -> new RuntimeException("Course not found"));
        courseModel.setCourseName(updatedCourseModel.getCourseName());
        courseModel.setDuration(updatedCourseModel.getDuration());
        courseModel.setDepartment(updatedCourseModel.getDepartment());
        return courseRepository.save(courseModel);
    }

    public void deleteCourse(long courseID) {
        courseRepository.deleteById(courseID);
    }

    // Get a course by ID
    public CourseModel getCourseById(Long courseID) {
        return courseRepository.findById(courseID).orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));
    }

    // Get all courses by department ID
    public List<CourseModel> getCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);  // Assuming you have this method in your repository
    }
}