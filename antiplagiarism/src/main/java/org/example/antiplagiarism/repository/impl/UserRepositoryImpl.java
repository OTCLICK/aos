package org.example.antiplagiarism.repository.impl;

import jakarta.persistence.EntityManager;
import org.example.antiplagiarism.entity.User;
import org.example.antiplagiarism.repository.BaseRepository;
import org.example.antiplagiarism.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, String> implements UserRepository {

    public UserRepositoryImpl(EntityManager entityManager) {
        super(entityManager, User.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                        .setParameter("email", email)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
        );
    }

    @Override
    public User save(User user) {
        return super.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return super.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return super.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0;
    }
}