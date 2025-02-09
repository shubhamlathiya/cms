package com.codershubham.cms.cms.service.CourseManagementModules;

import com.codershubham.cms.cms.model.CourseManagementModules.SyllabusModel;
import com.codershubham.cms.cms.repository.CourseManagementModules.SyllabusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SyllabusService {

    @Autowired
    private SyllabusRepository syllabusRepository;

//    public List<SyllabusModel> getAllSyllabus() {
//        return syllabusRepository.findAll();
//    }

    public Optional<SyllabusModel> getSyllabusById(Long id) {
        return syllabusRepository.findById(id);
    }

    public List<SyllabusModel> getSyllabusBySubject(Long subjectId) {
        return syllabusRepository.findBySubjectSubjectid(subjectId);
    }

    public SyllabusModel addSyllabus(SyllabusModel syllabus) {
        return syllabusRepository.save(syllabus);
    }

    public SyllabusModel updateSyllabus(Long id, SyllabusModel syllabusDetails) {
        return syllabusRepository.findById(id).map(syllabus -> {
            syllabus.setUnitNo(syllabusDetails.getUnitNo());
            syllabus.setUnitName(syllabusDetails.getUnitName());
            syllabus.setTopics(syllabusDetails.getTopics());
            syllabus.setResources(syllabusDetails.getResources());
            return syllabusRepository.save(syllabus);
        }).orElseThrow(() -> new RuntimeException("Syllabus not found with ID: " + id));
    }

    public void deleteSyllabus(Long id) {
        syllabusRepository.deleteById(id);
    }
}
