package com.tsq.service;

import com.tsq.beans.Article;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ArticleService {
    Article save(Article article);

    Page<Article> getByCondition(Map<String,Object> conditions);

    Article getById(String id);

    Boolean subscribe(Map map);

    Article saveById(String id, int i, String userId);
}
