package edu.icet.user_service.repository;

import edu.icet.user_service.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);  // Changed to Optional
    Optional<UserEntity> findByUsername(String username);  // Changed to Optional
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}