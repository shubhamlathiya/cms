package com.codershubham.cms.cms.repository;

import com.codershubham.cms.cms.model.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subjects, Long> {
//    Optional<Subjects> findByName(String name);
}
