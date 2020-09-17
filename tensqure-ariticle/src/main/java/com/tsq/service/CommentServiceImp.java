package com.tsq.service;

import com.mongodb.client.result.UpdateResult;
import com.tsq.beans.Comment;
import com.tsq.mongoMapper.CommentMapper;
import com.tsq.utils.SnowFlakeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImp implements CommentService{
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    SnowFlakeWorker snowFlakeWorker;
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public List<Comment> getAll() {
        List<Comment> all = commentMapper.findAll();
        return all;
    }

    @Override
    public Comment save(Comment comment) {
        long id = snowFlakeWorker.nextId();
//        comment.set_id(id+"");
        Comment comment1 = commentMapper.save(comment);
        return comment1;
    }

    @Override
    public Integer deleteById(String id) {
        commentMapper.deleteById(id);
        return null;
    }

    @Override
    public List<Comment> getByArticleId(String id) {
        List<Comment> commentList = commentMapper.findByArticleIdOrderByCreateDateDesc(id);
        return commentList;
    }

    @Override
    public Object thumbUpById(String id,Integer thumbNum) {

        Query query=new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(id));
        update.inc("thumbUp",thumbNum);
        UpdateResult comment = mongoTemplate.updateFirst(query, update, "comment");

        return comment;
    }

}
