package com.example.tms.repository;

import com.example.tms.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public  UserEntity findByEmail(String email);
}
