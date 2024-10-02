package com.writeitup.wiu_user_service.repository;

import com.writeitup.wiu_user_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
