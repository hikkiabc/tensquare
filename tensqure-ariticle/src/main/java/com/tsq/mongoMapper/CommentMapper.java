package com.tsq.mongoMapper;

import com.tsq.beans.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentMapper extends MongoRepository<Comment,String> {
    List<Comment> findByArticleIdOrderByCreateDateDesc(String id);
}
