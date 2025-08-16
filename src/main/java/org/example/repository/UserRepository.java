package org.example.repository;

import org.example.model.dto.UserDTO;
import org.example.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserDTO> findByEmail(String email);
}
