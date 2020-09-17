package com.tsq.mapper;

import com.tsq.beans.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ArticleMapper extends JpaRepository<Article,String>, JpaSpecificationExecutor<Article> {
@Query(value="update article set thumb_up=thumb_up+?2 where id=?1",nativeQuery=true)
    Article thumbUpById(String id, int i);
}
