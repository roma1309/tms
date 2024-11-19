package com.example.tms.service.impl;

import com.example.tms.dto.auth.SignupRequest;
import com.example.tms.entity.UserEntity;
import com.example.tms.entity.enums.Role;
import com.example.tms.exceptions.UserExistException;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserEntity createUser(SignupRequest signupRequest) {

        if (!signupRequest.getConfirmPassword().equals(signupRequest.getPassword())) {
            throw new UserExistException(HttpStatus.BAD_REQUEST, "wrong password", "Please check password and confirmPassword");
        }
        if (userRepo.findByEmail(signupRequest.getEmail()) != null) {
            throw new UserExistException(HttpStatus.BAD_REQUEST, "wrong email", "The user" + signupRequest.getEmail() + " already exist.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(signupRequest.getEmail());
        userEntity.setRole(Role.USER);
        userEntity.setName(signupRequest.getName());
        userEntity.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        return userRepo.save(userEntity);
    }
}
