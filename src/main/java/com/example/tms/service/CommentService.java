package com.example.tms.service;

import com.example.tms.dto.CommentDTO;
import com.example.tms.entity.CommentEntity;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    public CommentEntity createComment(Long taskId, CommentDTO commentDTO, Principal principal);
    public List<CommentEntity> getCommentsForUser(Long taskId,Principal principal);
}
