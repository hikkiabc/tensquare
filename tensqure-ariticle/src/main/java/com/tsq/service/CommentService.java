package com.tsq.service;

import com.tsq.beans.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getAll();

    Comment save(Comment comment);

    Integer deleteById(String id);

    List<Comment> getByArticleId(String id);

    Object thumbUpById(String id, Integer i);
}
