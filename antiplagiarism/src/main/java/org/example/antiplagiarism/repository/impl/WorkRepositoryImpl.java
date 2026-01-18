package org.example.antiplagiarism.repository.impl;

import jakarta.persistence.EntityManager;
import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.entity.enums.WorkStatus;
import org.example.antiplagiarism.repository.BaseRepository;
import org.example.antiplagiarism.repository.WorkRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkRepositoryImpl extends BaseRepository<Work, String> implements WorkRepository {

    public WorkRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Work.class);
    }

    @Override
    public List<Work> findByStudentId(String studentId) {
        return entityManager.createQuery(
                        "SELECT w FROM Work w WHERE w.student.id = :studentId", Work.class)
                .setParameter("studentId", studentId)
                .getResultList();
    }

    @Override
    public List<Work> findByStatus(WorkStatus status) {
        return entityManager.createQuery(
                        "SELECT w FROM Work w WHERE w.status = :status", Work.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public boolean existsByStudentIdAndTitle(String studentId, String title) {
        return !entityManager.createQuery(
                        "SELECT w FROM Work w WHERE w.student.id = :studentId AND w.title = :title", Work.class)
                .setParameter("studentId", studentId)
                .setParameter("title", title)
                .getResultList()
                .isEmpty();
    }

    @Override
    public Work save(Work work) {
        return super.save(work);
    }

    @Override
    public java.util.Optional<Work> findById(String id) {
        return super.findById(id);
    }

    @Override
    public List<Work> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }
}