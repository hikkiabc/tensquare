package com.tsq.mapper;

import com.tsq.beans.NoticeFresh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeFreshMapper extends JpaRepository<NoticeFresh,String> {
    @Query(value="select * from notice_fresh where user_id=?1",nativeQuery=true)
    Page<NoticeFresh> findByUserId(String id,PageRequest page);
}
