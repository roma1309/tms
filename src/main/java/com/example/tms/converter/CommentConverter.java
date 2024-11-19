package com.example.tms.converter;

import com.example.tms.dto.CommentDTO;
import com.example.tms.entity.CommentEntity;
import com.example.tms.entity.TaskEntity;
import com.example.tms.entity.UserEntity;

public class CommentConverter {

    public static CommentEntity dtoToEntity(CommentDTO commentDTO, UserEntity userEntity, TaskEntity taskEntity) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setTask(taskEntity);
        commentEntity.setText(commentDTO.getMessage());
        commentEntity.setUserId(userEntity.getId());
        return commentEntity;
    }

    public static CommentDTO EntityToDto(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setMessage(commentEntity.getText());
        commentDTO.setUserId(commentEntity.getUserId());
        return commentDTO;
    }
}
