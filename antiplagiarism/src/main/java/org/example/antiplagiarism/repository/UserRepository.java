package org.example.antiplagiarism.repository;

import org.example.antiplagiarism.entity.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    boolean existsById(String id);
    boolean existsByEmail(String email);
}