package com.example.tms.service.impl;

import com.example.tms.converter.CommentConverter;
import com.example.tms.dto.CommentDTO;
import com.example.tms.entity.CommentEntity;
import com.example.tms.entity.TaskEntity;
import com.example.tms.entity.UserEntity;
import com.example.tms.entity.enums.Role;
import com.example.tms.exceptions.TaskNotFoundException;
import com.example.tms.exceptions.UserExistException;
import com.example.tms.repository.CommentRepository;
import com.example.tms.repository.TaskRepository;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final TaskRepository taskRepo;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepo, UserRepository userRepo, TaskRepository taskRepo) {
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
    }

    @Override
    public CommentEntity createComment(Long taskId, CommentDTO commentDTO, Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        TaskEntity taskEntity = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id", "task not found"));
        if (userEntity.getRole().equals(Role.ADMIN) && taskEntity.getCreatedAdminId() == userEntity.getId()) {
            CommentEntity commentEntity = CommentConverter.dtoToEntity(commentDTO, userEntity, taskEntity);
            return commentRepo.save(commentEntity);
        } else if (userEntity.getRole().equals(Role.USER)) {
            Optional<UserEntity> user = taskEntity.getUsers().stream().filter(u -> u.equals(userEntity)).findFirst();
            if (user != null) {
                CommentEntity commentEntity = CommentConverter.dtoToEntity(commentDTO, userEntity, taskEntity);
                return commentRepo.save(commentEntity);
            }
        }
        throw new UserExistException(HttpStatus.BAD_REQUEST, "forbidden ", "this user does not have access rights");
    }

    @Override
    public List<CommentEntity> getCommentsForUser(Long taskId, Principal principal) {
        UserEntity userEntity = getUserByPrincipal(principal);
        TaskEntity taskEntity = taskRepo.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(HttpStatus.BAD_REQUEST, "wrong id", "task not found"));
        if (userEntity.getRole().equals(Role.ADMIN) && taskEntity.getCreatedAdminId() == userEntity.getId()) {
            return taskEntity.getComments();
        } else if (userEntity.getRole().equals(Role.USER)) {
            Optional<UserEntity> user = taskEntity.getUsers().stream().filter(u -> u.equals(userEntity)).findFirst();
            if (user != null) {
                return taskEntity.getComments();
            }

        }
        throw new UserExistException(HttpStatus.BAD_REQUEST, "forbidden ", "this user does not have access rights");
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
