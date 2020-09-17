package com.tsq.controller;

import com.tsq.beans.User;
import com.tsq.service.UserService;
import com.tsq.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public R getAll(){
        return null;
    }
    @GetMapping("/{id}")
    public R getById(@PathVariable String id){
       User user= userService.getById(id);
       return R.data(user);
    }
}
