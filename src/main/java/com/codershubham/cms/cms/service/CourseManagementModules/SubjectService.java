package com.codershubham.cms.cms.service.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.CourseModel;
import com.codershubham.cms.cms.model.CourseManagementModules.SubjectsModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.CourseRepository;
import com.codershubham.cms.cms.repository.CourseManagementModules.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;  // Inject CourseRepository to get course details

    // Create a new subject
    public SubjectsModel createSubject(SubjectsModel subject) {
        // Ensure the course exists and fetch it using courseID
        Long courseID = subject.getCourse().getCourseID();  // Ensure courseID is being passed properly
        CourseModel courseModel = courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));

        subject.setCourse(courseModel);  // Set the course for the subject
        subject.setCreatedAt(LocalDateTime.now());  // Set the creation timestamp for the subject

        return subjectRepository.save(subject);  // Save and return the created subject
    }

    // Get all subjects
    public List<SubjectsModel> getAllSubjects() {
        return subjectRepository.findAll();  // Retrieve all subjects from the database
    }

    // Update an existing subject
    public SubjectsModel updateSubject(long subjectID, SubjectsModel updatedSubject) {
        // Find the subject by ID or throw an exception if not found
        SubjectsModel subject = subjectRepository.findById(subjectID)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectID));

        // Ensure the course exists and fetch it using the courseID from updatedSubject
        Long courseID = updatedSubject.getCourse().getCourseID();
        CourseModel courseModel = courseRepository.findById(courseID)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseID));

        // Update the subject fields
        subject.setSubjectCode(updatedSubject.getSubjectCode());
        subject.setSubjectName(updatedSubject.getSubjectName());
        subject.setCredits(updatedSubject.getCredits());
        subject.setCourse(courseModel);
        subject.setSubjectType(updatedSubject.getSubjectType());// Set the updated course

        return subjectRepository.save(subject);  // Save and return the updated subject
    }

    // Delete a subject by ID
    public void deleteSubject(long subjectID) {
        // Check if the subject exists before deleting
        if (!subjectRepository.existsById(subjectID)) {
            throw new RuntimeException("Subject not found with ID: " + subjectID);
        }

        subjectRepository.deleteById(subjectID);  // Delete the subject from the database
    }

    public List<SubjectsModel> findAllById(List<Long> subjectIds) {
        return subjectRepository.findAllById(subjectIds);
    }

    // Get a subject by ID
    public SubjectsModel getSubjectById(long subjectID) {
        return subjectRepository.findById(subjectID)
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + subjectID));  // Return subject if exists
    }

    public List<SubjectsModel> getSubjectsByCourseId(Long courseId) {
        CourseModel course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return subjectRepository.findByCourseModel(course);
    }

//    public List<SubjectsModel> getAssignedSubjectsBySemester(Long semesterId) {
//        return subjectRepository.findSubjectsAssignedToFaculty(semesterId);
//    }
//
//    public List<SubjectsModel> getUnassignedSubjectsBySemester(Long semesterId) {
//        return subjectRepository.findSubjectsNotAssignedToFaculty(semesterId);
//    }
}
