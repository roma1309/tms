package com.example.tms.service;

import com.example.tms.dto.TaskDto;
import com.example.tms.entity.TaskEntity;

import java.security.Principal;
import java.util.List;

public interface TaskService {

    public TaskEntity createTask(TaskDto taskDto, Principal principal);

    public void deleteTask(Long id);

    public List<TaskEntity> getTasks();

    public List<TaskEntity> findTaskForUser(Principal principal, String status, String priority);

    public TaskEntity updateStatusPriority(String status, String priority, Long taskId);

    public TaskEntity setUserForTask(Long userId, Long taskId, Principal principal);

    public TaskEntity setStatusFromUser(Principal principal, String status, Long taskId);
}
