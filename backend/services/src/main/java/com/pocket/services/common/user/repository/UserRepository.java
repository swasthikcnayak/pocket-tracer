package com.pocket.services.common.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pocket.services.common.security.dto.UserInfo;
import com.pocket.services.common.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserInfo> findUserInfoByEmail(String email);
}
