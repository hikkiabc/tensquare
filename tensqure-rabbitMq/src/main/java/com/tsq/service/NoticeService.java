package com.tsq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsq.beans.Notice;
import com.tsq.beans.NoticeFresh;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    Page getByCondition(Map map) throws JsonProcessingException;

    Map saveNotice(Notice notice);

    Page<NoticeFresh> getNoticeFreshByUserId(String userId, Integer pageNum, Integer pageSize);

    int deleteNoticeFresh(NoticeFresh noticeFresh);

    Notice getById(String id);
}
