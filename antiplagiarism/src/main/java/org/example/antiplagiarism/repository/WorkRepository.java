package org.example.antiplagiarism.repository;

import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.entity.enums.WorkStatus;
import java.util.List;
import java.util.Optional;

public interface WorkRepository {
    Work save(Work work);
    Optional<Work> findById(String id);
    List<Work> findAll();
    void deleteById(String id);
    List<Work> findByStudentId(String studentId);
    List<Work> findByStatus(WorkStatus status);
    boolean existsByStudentIdAndTitle(String studentId, String title);
}