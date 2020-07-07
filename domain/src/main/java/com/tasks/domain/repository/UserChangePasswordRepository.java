package com.tasks.domain.repository;

import com.tasks.domain.model.UserChangePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChangePasswordRepository extends JpaRepository<UserChangePassword, Long> {
}
