package com.tsq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tsq.beans.Notice;
import com.tsq.beans.NoticeFresh;
import com.tsq.service.NoticeService;
import com.tsq.utils.R;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;
    @GetMapping
    public R getById(String id){
      Notice notice=  noticeService.getById(id);
      return R.data(notice);
    }
    @PostMapping("/condition")
    public R getNoticeByCondition(@RequestBody Map<String, Object> map) throws JsonProcessingException {

        Page page = noticeService.getByCondition(map);
        return R.data(page);
    }
    @PostMapping
    public R saveNotice(@RequestBody Notice notice){
       Map map= noticeService.saveNotice(notice);
       return R.data(map);
    }
    @GetMapping("/noticefresh")
    public R getNoticeFreshByUserId(String userId,  Integer pageSize, Integer pageNum){
        Page<NoticeFresh> noticeFreshPage=
              noticeService.getNoticeFreshByUserId(userId,pageNum,pageSize);
      return R.data(noticeFreshPage);
    }
    @DeleteMapping("noticefresh")
    public R deleteNoticeFresh(@RequestBody NoticeFresh noticeFresh){

     int res=   noticeService.deleteNoticeFresh(noticeFresh);
return R.data(res);
    }
}
