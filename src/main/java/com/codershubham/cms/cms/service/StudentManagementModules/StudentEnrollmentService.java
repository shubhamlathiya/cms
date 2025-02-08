package com.codershubham.cms.cms.service.StudentManagementModules;


import com.codershubham.cms.cms.model.StudentManagementModules.DivisionModel;
import com.codershubham.cms.cms.model.StudentManagementModules.SemesterModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentEnrollmentModel;
import com.codershubham.cms.cms.model.StudentManagementModules.StudentModel;
import com.codershubham.cms.cms.repository.StudentManagementModules.StudentEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentEnrollmentService {

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    public void enrollStudent(StudentEnrollmentModel enrollment) {
        studentEnrollmentRepository.save(enrollment);
    }

//    public List<StudentEnrollmentModel> getStudentsBySemester(SemesterModel semester) {
//        return studentEnrollmentRepository.findBySemester(semester);
//    }
//
//    public List<StudentEnrollmentModel> getStudentsByDivision(DivisionModel division) {
//        return studentEnrollmentRepository.findByDivision(division);
//    }
//
//    public List<StudentEnrollmentModel> getStudentsBySemesterAndDivision(SemesterModel semester, DivisionModel division) {
//        return studentEnrollmentRepository.findBySemesterAndDivision(semester, division);
//    }
}
