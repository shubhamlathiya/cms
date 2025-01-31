package com.codershubham.cms.cms.service;

import com.codershubham.cms.cms.model.Course;
import com.codershubham.cms.cms.model.Subjects;
import com.codershubham.cms.cms.repository.CourseRepository;
import com.codershubham.cms.cms.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;  // Inject CourseRepository

    // Create a new subject
    public Subjects createSubject(Subjects subject) {
        // Ensure courseID exists and fetch the corresponding course
        Long courseID = (long) subject.getCourse().getCourseID();  // Assuming course is passed with courseID
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));

        subject.setCourse(course);  // Set the fetched course to the subject
        subject.setCreatedAt(LocalDateTime.now()); // Set the creation timestamp

        return subjectRepository.save(subject); // Save the subject to the database
    }

    // Get all subjects
    public List<Subjects> getAllSubjects() {
        return subjectRepository.findAll(); // Retrieve all subjects from the database
    }

    // Update an existing subject
    public Subjects updateSubject(long subjectID, Subjects updatedSubject) {
        // Find the subject by ID or throw an exception if not found
        Subjects subject = subjectRepository.findById(subjectID)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectID));

        // Ensure courseID exists and fetch the corresponding course
        Long courseID = (long) updatedSubject.getCourse().getCourseID();
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));

        // Update the subject details
        subject.setSubjectCode(updatedSubject.getSubjectCode());
        subject.setSubjectName(updatedSubject.getSubjectName());
        subject.setCredits(updatedSubject.getCredits());
        subject.setCourse(course);  // Set the updated course

        return subjectRepository.save(subject); // Save the updated subject
    }

    // Delete a subject by ID
    public void deleteSubject(long subjectID) {
        // Check if the subject exists before deleting
        if (!subjectRepository.existsById(subjectID)) {
            throw new RuntimeException("Subject not found with ID: " + subjectID);
        }

        subjectRepository.deleteById(subjectID); // Delete the subject
    }
}
