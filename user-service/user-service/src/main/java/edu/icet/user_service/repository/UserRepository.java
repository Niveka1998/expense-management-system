package edu.icet.user_service.repository;

import edu.icet.user_service.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
}
