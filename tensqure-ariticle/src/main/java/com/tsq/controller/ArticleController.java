package com.tsq.controller;

import com.tsq.beans.Article;
import com.tsq.beans.Comment;
import com.tsq.service.ArticleService;
import com.tsq.service.CommentService;
import com.tsq.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/article")
//@CrossOrigin
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    CommentService commentService;
    @Autowired
    RedisTemplate redisTemplate;
@PostMapping("/thumbup/{id}")
public R articleThumbUp(@PathVariable String id){
    String userId="userId";
    Object flag = redisTemplate.opsForValue().get("articleThumb_" + id + userId);
    if (flag!=null){
        redisTemplate.delete("articleThumb_" + id + userId);
      Article article=  articleService.saveById(id,-1,userId);
      return R.data("thumbUp");
    }else {
        redisTemplate.opsForValue().set("articleThumb_" + id + userId,"thumbUp");
        articleService.saveById(id,1,userId);
        return R.data("no thumbUp");
    }

}
    @PostMapping("/subscribe")
    public R subscribe(@RequestBody Map map){
     Boolean subscribe=   articleService.subscribe(map);
     if (subscribe){
         return R.data("sub");
     }return R.data("unsub");
    }
    @PostMapping
    public R addArticle(@RequestBody Article article){
      Article article1=  articleService.save(article);
        R r = new R();
        r.setData(article1);
        return r;
    }
        @GetMapping("/{id}")
        public R getById(@PathVariable String id){
       Article article= articleService.getById(id);
        return R.data(article);
        }

    @PostMapping("/condition")
    public R getAllArticleWithCondition(@RequestBody Map<String,Object> conditions){

        Page<Article> articlePage = articleService.getByCondition(conditions);
        R r = new R();
        r.setData(articlePage);
        return r;
    }
    @GetMapping("/comment")
    public R findComment(){
    List<Comment> commentList=    commentService.getAll();
        R r = new R();
        r.setData(commentList);
        return r;
    }
    @PostMapping("/comment")
    public R saveComment(@RequestBody Comment comment){
       Comment comment1= commentService.save(comment);
        R data = R.data(comment1);
        return data;
    }
    @PutMapping("/comment")
    public R updateComment(@RequestBody Comment comment){
        Comment comment1= commentService.save(comment);
        R data = R.data(comment1);
        return data;
    }
    @DeleteMapping("/comment/{id}")
    public R deleteComment(@PathVariable String id){
      Integer res=  commentService.deleteById(id);
      return R.data(res);
    }
    @GetMapping("/comment/{id}")
    public R getCommentByArticleId(@PathVariable String id){
      List<Comment>  commentList= commentService.getByArticleId(id);
      return R.data(commentList);
    }
    @PutMapping("/commentthumb/{id}")
    public R thumbUpById(@PathVariable String id){
       String userId="userId";
       Object res;
        Object thumbed = redisTemplate.opsForValue().get("thumb_" + userId + id);
        if (thumbed==null){
             res= commentService.thumbUpById(id,1);
            redisTemplate.opsForValue().set("thumb_" + userId + id,"1");
        }
        else {
            System.out.println(redisTemplate.getExpire("thumb_" + userId + id));
            res= commentService.thumbUpById(id,-1);
            redisTemplate.delete("thumb_" + userId + id);
        }
       return R.data(res);
    }

}
