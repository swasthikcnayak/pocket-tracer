package com.pocket.services.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pocket.services.security.dto.UserInfo;
import com.pocket.services.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<UserInfo> findUserInfoByEmail(String email);
}
