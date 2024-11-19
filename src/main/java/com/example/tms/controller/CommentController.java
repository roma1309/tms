package com.example.tms.controller;

import com.example.tms.config.validation.ResponseErrorValidation;
import com.example.tms.converter.CommentConverter;
import com.example.tms.dto.CommentDTO;
import com.example.tms.entity.CommentEntity;
import com.example.tms.exceptions.ValidException;
import com.example.tms.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
@Tag(name = "comments")
@SecurityRequirement(name = "JWT")
public class CommentController {
    private final CommentService commentService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @Operation(
            description = "добавление комментариев",
            summary = "может либо админ создавший задачу, либо пользователи выполняющие задачу"

    )
    @PostMapping("/{task_id}/create")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO,BindingResult bindingResult,
                                                    @PathVariable("task_id") final String taskId,  Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            throw new ValidException(HttpStatus.BAD_REQUEST, "Validation error", "Incorrectly filled in fields");
        }
        CommentEntity commentEntity = commentService.createComment(Long.parseLong(taskId), commentDTO, principal);
        return new ResponseEntity<>(CommentConverter.EntityToDto(commentEntity), HttpStatus.CREATED);
    }

    @Operation(
            description = "просмотр комментариев задачи",
            summary = "могут только пользователи выполняющие задачу или админ создавший ее"

    )
    @GetMapping("/{task_id}")
    public ResponseEntity<List<CommentDTO>> getComment(@PathVariable("task_id") final String taskId, Principal principal) {
        List<CommentEntity> commentEntity = commentService.getCommentsForUser(Long.parseLong(taskId), principal);
        return new ResponseEntity<>(commentEntity.stream().map(CommentConverter::EntityToDto).collect(Collectors.toList()), HttpStatus.CREATED);
    }
}
