package com.tsq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsq.beans.Notice;
import com.tsq.beans.NoticeFresh;
import com.tsq.feign.ArticleFeign;
import com.tsq.feign.UserFeign;
import com.tsq.mapper.NoticeFreshMapper;
import com.tsq.mapper.NoticeMapper;
import com.tsq.utils.R;
import com.tsq.utils.SnowFlakeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class NoticeServiceImp implements NoticeService {
    @Autowired
    NoticeMapper noticeMapper;
    @Autowired
    NoticeFreshMapper noticeFreshMapper;
@Autowired
    SnowFlakeWorker snowFlakeWorker;
@Autowired
    UserFeign userFeign;
@Autowired
    ArticleFeign articleFeign;

private void getNoticeInfo(Notice notice){
    Map user = (Map) userFeign.getById(notice.getOperatorId()).getData();
    System.out.println(user);
    Map article = (Map) articleFeign.getById(notice.getTargetId()).getData();
    notice.setTargetName((String)article.get("name"));
    notice.setOperatorName(user.get("username").toString());
}

    @Override
    public Page getByCondition(Map map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object notice1 = map.get("notice");
        String string = objectMapper.writeValueAsString(notice1);
        Notice notice = objectMapper.readValue(string, Notice.class);
        Integer pageSize = (Integer) map.get("pageSize");
        Integer pageNum = (Integer) map.get("pageNum");
        notice.setStartTime(null);
        notice.setState(null);
        Page<Notice> noticePage = noticeMapper.findAll(Example.of(notice),
                PageRequest.of(pageNum - 1, pageSize, Sort.by("id").ascending()));
        List<Notice> content = noticePage.getContent();
        for (Notice notice2 : content) {
            getNoticeInfo(notice2);
        }
        return noticePage;

    }

    @Override
    public Map saveNotice(Notice notice) {
        Map<Object, Object> map = new HashMap<>();
        Notice notice1 = noticeMapper.save(notice);
//        String id = notice1.getId();
//        NoticeFresh noticeFresh = new NoticeFresh();
//        noticeFresh.setNoticeId(id);
//        noticeFresh.setUserId(notice1.getReceiverId());
//        NoticeFresh noticeFresh1 = noticeFreshMapper.save(noticeFresh);
//        map.put("noticeFresh1",noticeFresh1);
        map.put("notice",notice1);

        return map;
    }

    @Override
    public Page<NoticeFresh> getNoticeFreshByUserId(String userId, Integer pageNum, Integer pageSize) {
        PageRequest of = PageRequest.of(pageNum - 1, pageSize);
        Page<NoticeFresh> noticeFreshPage =
                noticeFreshMapper.findByUserId(userId,of);
        return noticeFreshPage;
    }

    @Override
    public int deleteNoticeFresh(NoticeFresh noticeFresh) {
        noticeFreshMapper.delete(noticeFresh);
        return 0;
    }

    @Override
    public Notice getById(String id) {
        Notice notice = noticeMapper.findById(id).get();
        getNoticeInfo(notice);
        return notice;
    }

}
