package com.example.tms.service;

import com.example.tms.dto.auth.SignupRequest;
import com.example.tms.entity.UserEntity;

public interface UserService {
    public UserEntity createUser(SignupRequest signupRequest);
}
