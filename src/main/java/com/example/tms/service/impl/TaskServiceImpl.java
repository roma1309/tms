package com.example.tms.service.impl;

import com.example.tms.controller.TaskController;
import com.example.tms.converter.TaskConverter;
import com.example.tms.dto.TaskDto;
import com.example.tms.entity.TaskEntity;
import com.example.tms.entity.UserEntity;
import com.example.tms.entity.enums.Priority;
import com.example.tms.entity.enums.Role;
import com.example.tms.entity.enums.Status;
import com.example.tms.exceptions.TaskException;
import com.example.tms.exceptions.TaskNotFoundException;
import com.example.tms.exceptions.UserExistException;
import com.example.tms.repository.TaskRepository;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @Override
    public TaskEntity createTask(TaskDto taskDto, Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        TaskEntity taskEntity = TaskConverter.dtoToEntity(taskDto, userEntity.getId());
        return taskRepo.save(taskEntity);
    }

    @Override
    public void deleteTask(Long id) {
        TaskEntity taskEntity = taskRepo.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id", "task not found"));
        taskRepo.delete(taskEntity);
    }

    @Override
    public List<TaskEntity> getTasks() {
        return taskRepo.findAll();
    }

    @Override
    public List<TaskEntity> findTaskForUser(Principal principal, String status, String priority) {
        UserEntity userEntity = getUserByPrincipal(principal);
        if (userEntity.getRole().equals(Role.ADMIN)) {
            if (status != null && priority != null) {
                return taskRepo.findByCreatedAdminIdAndPriorityAndStatus(userEntity.getId(), Priority.getPriority(priority), Status.getStatus(status));
            } else if (status != null) {
                return taskRepo.findByCreatedAdminIdAndStatus(userEntity.getId(), Status.getStatus(status));
            } else if (priority != null) {
                return taskRepo.findByCreatedAdminIdAndPriority(userEntity.getId(), Priority.getPriority(priority));
            } else {
                return taskRepo.findByCreatedAdminId(userEntity.getId());
            }
        } else {
            if (status != null && priority != null) {
                return taskRepo.findByUsersAndPriorityAndStatus(userEntity, Priority.getPriority(priority), Status.getStatus(status));
            } else if (status != null) {
                return taskRepo.findByUsersAndStatus(userEntity, Status.getStatus(status));
            } else if (priority != null) {
                return taskRepo.findByUsersAndPriority(userEntity, Priority.getPriority(priority));
            } else {
                return taskRepo.findByUsers(userEntity);
            }
        }
    }

    @Override
    public TaskEntity updateStatusPriority(String status, String priority, Long taskId) {
        TaskEntity taskEntity = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id", "task not found"));
        if (status != null) {
            taskEntity.setStatus(Status.getStatus(status));
        }
        if (priority != null) {
            taskEntity.setPriority(Priority.getPriority(priority));
        }
        return taskRepo.save(taskEntity);
    }

    @Override
    public TaskEntity setUserForTask(Long userId, Long taskId, Principal principal) {
        UserEntity admin = getUserByPrincipal(principal);
        TaskEntity taskEntity = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id", "task not found"));
        if (admin.getId() != taskEntity.getCreatedAdminId()){
            throw new UserExistException(HttpStatus.BAD_REQUEST, "this admin did not create this task ", " wrong admin");
        }

        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new UserExistException(HttpStatus.BAD_REQUEST, "wrong id", "user not found"));
        Optional<UserEntity> user = taskEntity.getUsers().stream().filter(u -> u.equals(userEntity)).findFirst();

        if (!user.isPresent()) {
            taskEntity.getUsers().add(userEntity);
        } else {
            throw new TaskException(HttpStatus.BAD_REQUEST, "User already consist", " wrong user");
        }
        return taskRepo.save(taskEntity);
    }

    @Override
    public TaskEntity setStatusFromUser(Principal principal, String status, Long taskId) {
        UserEntity userEntity = getUserByPrincipal(principal);
        TaskEntity taskEntity = taskRepo.findByIdAndUsers(taskId, userEntity);
        if (taskEntity == null) {
            throw new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id and user", "task not found for this user");
        }
        taskEntity.setStatus(Status.getStatus(status));
        return taskRepo.save(taskEntity);
    }

    private UserEntity getUserByPrincipal(Principal principal) {
        String email = principal.getName();
        UserEntity userEntity = userRepo.findByEmail(email);
        if (userEntity == null) {
            new UsernameNotFoundException("User not found with email - " + email);
        }
        return userEntity;
    }
}
