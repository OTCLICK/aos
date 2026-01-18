package org.example.antiplagiarism.repository.impl;

import jakarta.persistence.EntityManager;
import org.example.antiplagiarism.entity.Grade;
import org.example.antiplagiarism.repository.BaseRepository;
import org.example.antiplagiarism.repository.GradeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GradeRepositoryImpl extends BaseRepository<Grade, String> implements GradeRepository {

    public GradeRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Grade.class);
    }

    @Override
    public Optional<Grade> findByWorkId(String workId) {
        return Optional.ofNullable(
                entityManager.createQuery(
                                "SELECT g FROM Grade g WHERE g.work.id = :workId", Grade.class)
                        .setParameter("workId", workId)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
        );
    }

    @Override
    public Grade save(Grade grade) {
        return super.save(grade);
    }

    @Override
    public List<Grade> findAll() {
        return entityManager.createQuery(
                        "SELECT g FROM Grade g", Grade.class)
                .getResultList();
    }
}