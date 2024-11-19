package com.example.tms.repository;

import com.example.tms.entity.TaskEntity;
import com.example.tms.entity.UserEntity;
import com.example.tms.entity.enums.Priority;
import com.example.tms.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    public TaskEntity findByIdAndUsers(Long id, UserEntity userEntity);

    public List<TaskEntity> findByCreatedAdminId(Long adminId);

    public List<TaskEntity> findByCreatedAdminIdAndStatus(Long adminId, Status status);

    public List<TaskEntity> findByCreatedAdminIdAndPriority(Long adminId, Priority priority);

    public List<TaskEntity> findByUsersAndPriorityAndStatus(UserEntity userEntity, Priority priority, Status status);

    public List<TaskEntity> findByUsers(UserEntity userEntity);

    public List<TaskEntity> findByUsersAndPriority(UserEntity userEntity, Priority priority);

    public List<TaskEntity> findByUsersAndStatus(UserEntity userEntity, Status status);

    public List<TaskEntity> findByCreatedAdminIdAndPriorityAndStatus(Long adminId, Priority priority, Status status);



}
