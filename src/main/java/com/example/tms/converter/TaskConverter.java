package com.example.tms.converter;

import com.example.tms.dto.TaskDto;
import com.example.tms.entity.TaskEntity;
import com.example.tms.entity.UserEntity;
import com.example.tms.entity.enums.Priority;
import com.example.tms.entity.enums.Status;

public class TaskConverter {

    public static TaskEntity dtoToEntity(TaskDto taskDto, Long adminId) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription(taskDto.getDescription());
        taskEntity.setName(taskDto.getName());
        taskEntity.setStatus(Status.getStatus(taskDto.getStatus()));
        taskEntity.setPriority(Priority.getPriority(taskDto.getPriority()));
        taskEntity.setCreatedAdminId(adminId);
        return taskEntity;
    }

    public static TaskDto entityToDto(TaskEntity taskEntity) {
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setName(taskEntity.getName());
        taskDto.setPriority(taskEntity.getPriority().name());
        taskDto.setStatus(taskEntity.getStatus().name());
        taskDto.setId(taskEntity.getId());
        return taskDto;
    }

}
