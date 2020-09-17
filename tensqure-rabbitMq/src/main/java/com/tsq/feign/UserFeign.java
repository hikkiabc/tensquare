package com.tsq.feign;

import com.tsq.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ts-user")
public interface UserFeign {
    @GetMapping("user/{id}")
    public R getById(@PathVariable("id") String id);
}
