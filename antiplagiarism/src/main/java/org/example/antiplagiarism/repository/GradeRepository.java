package org.example.antiplagiarism.repository;

import org.example.antiplagiarism.entity.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeRepository {
    Grade save(Grade grade);
    Optional<Grade> findByWorkId(String workId);
    List<Grade> findAll();
    void deleteByWorkId(String workId);


}