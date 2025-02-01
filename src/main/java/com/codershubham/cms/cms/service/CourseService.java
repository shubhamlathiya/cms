package com.codershubham.cms.cms.service;

import com.codershubham.cms.cms.model.Course;
import com.codershubham.cms.cms.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course createCourse(Course course) {
        course.setCreatedAt(LocalDateTime.now());
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long courseID, Course updatedCourse) {
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setCourseName(updatedCourse.getCourseName());
        course.setDuration(updatedCourse.getDuration());
        course.setDepartment(updatedCourse.getDepartment());
        return courseRepository.save(course);
    }

    public void deleteCourse(long courseID) {
        courseRepository.deleteById(courseID);
    }

    // Get a course by ID
    public Course getCourseById(Long courseID) {
        return courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));
    }

    // Get all courses by department ID
    public List<Course> getCoursesByDepartment(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);  // Assuming you have this method in your repository
    }
}