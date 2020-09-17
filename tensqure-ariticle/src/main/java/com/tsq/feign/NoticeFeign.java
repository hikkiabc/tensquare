package com.tsq.feign;


import com.tsq.beans.Notice;
import com.tsq.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("tsq-rabbitMq")
public interface NoticeFeign {
    @PostMapping("/notice")
    public R saveNotice(@RequestBody Notice notice);
}
