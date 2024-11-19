package com.example.tms.controller;

import com.example.tms.converter.TaskConverter;
import com.example.tms.dto.TaskDto;
import com.example.tms.entity.TaskEntity;
import com.example.tms.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@Tag(name = "task")
@SecurityRequirement(name = "JWT")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            description = "создание задачи",
            summary = "только админ может создать ее"

    )
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto, Principal principal) {
        TaskEntity taskEntity = taskService.createTask(taskDto, principal);
        return new ResponseEntity<>(TaskConverter.entityToDto(taskEntity), HttpStatus.CREATED);
    }
    @Operation(
            description = "удаление задачи",
            summary = "может удалить любой админ"

    )
    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<String> deleteTask(@PathVariable("task_id") final String taskId) {
        taskService.deleteTask(Long.parseLong(taskId));
        return new ResponseEntity<>("Task was deleted", HttpStatus.OK);
    }

    @Operation(
            description = "присвоение задаче пользователя",
            summary = "присваивать может только админ создавший задачу"

    )
    @PutMapping("/set/user")
    public ResponseEntity<TaskDto> setUserTask(@RequestParam(value = "user_id") final String userId,
                                               @RequestParam(value = "task_id") final String taskId, Principal principal) {
        TaskEntity taskEntity = taskService.setUserForTask(Long.parseLong(userId), Long.parseLong(taskId), principal);
        return new ResponseEntity<>(TaskConverter.entityToDto(taskEntity), HttpStatus.CREATED);
    }

    @Operation(
            description = "изменение статуса или приоритета",
            summary = "изменять может только админ создавший задачу, можно обновлять сразу приоритет и статус либо на выбор"

    )
    @PutMapping("/update/{task_id}")
    public ResponseEntity<TaskDto> updateStatusPriorityTask(@RequestParam(value = "status", required = false) final String status,
                                                            @RequestParam(value = "priority", required = false) final String priority,
                                                            @PathVariable("task_id") final String taskId) {
        TaskEntity taskEntity = taskService.updateStatusPriority(status, priority, Long.parseLong(taskId));
        return new ResponseEntity<>(TaskConverter.entityToDto(taskEntity), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getTasks().stream()
                .map(TaskConverter::entityToDto).collect(Collectors.toList());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(
            description = "получение задач",
            summary = "админ получает задачи, которые он создал, а пользователь которые выполняет"

    )
    @GetMapping("/user")
    public ResponseEntity<List<TaskDto>> getTasksForUser(@RequestParam(value = "status", required = false) final String status,
                                                         @RequestParam(value = "priority", required = false) final String priority,
                                                         Principal principal) {
        List<TaskDto> tasks = taskService.findTaskForUser(principal, status, priority).stream()
                .map(TaskConverter::entityToDto).collect(Collectors.toList());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @Operation(
            description = "изменение статуса",
            summary = "только для пользователей выполняющих задачу"

    )
    @PutMapping("/update/{task_id}/user")
    public ResponseEntity<TaskDto> updateStatusTaskFromUser(@RequestParam(value = "status") final String status,
                                                            @PathVariable("task_id") final String taskId, Principal principal) {
        TaskEntity taskEntity = taskService.setStatusFromUser(principal, status, Long.parseLong(taskId));
        return new ResponseEntity<>(TaskConverter.entityToDto(taskEntity), HttpStatus.OK);
    }
}
