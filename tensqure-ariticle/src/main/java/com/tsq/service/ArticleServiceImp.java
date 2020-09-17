package com.tsq.service;

import com.tsq.beans.Article;
import com.tsq.beans.Notice;
import com.tsq.feign.NoticeFeign;
import com.tsq.mapper.ArticleMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@Transactional
public class ArticleServiceImp implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    NoticeFeign noticeFeign;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public Article save(Article article) {
        String authorId = "1";
        Article article1 = articleMapper.save(article);
        Set<String> members = redisTemplate.boundSetOps("subscribers" + authorId).members();
        if (null != members && members.size() > 0) {
//        List<String> members=Arrays.asList("aa", "bb", "cc");
            Notice notice = new Notice();
            for (String member : members) {
                notice.setOperatorId(authorId);
                notice.setTargetType(Notice.TARGET_TYPE_ARTICLE);
                notice.setAction(Notice.ACTION_PUBLISH);
                notice.setReceiverId(member);
                notice.setTargetId(article1.getId());
                noticeFeign.saveNotice(notice);
            }
            rabbitTemplate.convertAndSend("article_subscribe", authorId, article1.getId() + ", has new publish");
        }
        return article1;
    }

    @Override
    public Page<Article> getByCondition(Map<String, Object> conditions) {
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by("id").descending());
        Specification<Article> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Set<String> keys = conditions.keySet();
                for (String key : keys) {
                    predicates.add(cb.equal(root.get(key), conditions.get(key)));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<Article> articlePage = articleMapper.findAll(specification, pageRequest);

        return articlePage;
    }

    @Override
    public Article getById(String id) {
        Optional<Article> article = articleMapper.findById(id);
        return article.get();
    }

    @Override
    public Boolean subscribe(Map map) {
        String userId = (String) map.get("userId");
        String articleId = (String) map.get("articleId");
        Article article = articleMapper.findById(articleId).get();
        String authorId = article.getAuthorId();
        Boolean isMember = redisTemplate.boundSetOps("authorIds" + userId).isMember(authorId);
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        DirectExchange exchange = new DirectExchange("article_subscribe");
        rabbitAdmin.declareExchange(exchange);
        Queue queue = new Queue("article_subscribe_" + userId, true);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(authorId);
        if (isMember) {
            redisTemplate.boundSetOps("authorIds" + userId).remove(authorId);
            redisTemplate.boundSetOps("subscribers" + authorId).remove(userId);
            rabbitAdmin.removeBinding(binding);
            return false;
        } else {
            redisTemplate.boundSetOps("authorIds" + userId).add(authorId);
            redisTemplate.boundSetOps("subscribers" + authorId).add(userId);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(binding);
            return true;
        }
    }

    @Override
    public Article saveById(String id, int i, String userId) {
//      Article article=  articleMapper.thumbUpById(id,i);
        Article article1 = articleMapper.findById(id).get();
        article1.setThumbUp(article1.getThumbUp() + i);
        Article article2 = articleMapper.save(article1);
        if (i > 0) {
            Notice notice = new Notice();
            notice.setReceiverId(article1.getAuthorId());
            notice.setOperatorId(userId);
            noticeFeign.saveNotice(notice);

        }
        RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        Queue queue = new Queue("article_thumb_" + article1.getAuthorId(), true);
        rabbitAdmin.declareQueue(queue);
        rabbitTemplate.convertAndSend("article_thumb_" + article1.getAuthorId(), article1.getId());
        return article2;
    }
}
