package com.tsq.service;

import com.tsq.beans.User;
import com.tsq.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImp implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User getById(String id) {
        User user = userMapper.findById(id).get();
        return user;
    }
}
