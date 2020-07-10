package com.tasks.domain.service;

import com.tasks.domain.model.UserChangePassword;
import com.tasks.domain.repository.UserChangePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserChangePasswordService {

    private final UserChangePasswordRepository userChangePasswordRepository;

    @Autowired
    public UserChangePasswordService(UserChangePasswordRepository userChangePasswordRepository) {
        this.userChangePasswordRepository = userChangePasswordRepository;
    }

    public void save(UserChangePassword userChangePassword) {
        this.userChangePasswordRepository.save(userChangePassword);
    }
}
