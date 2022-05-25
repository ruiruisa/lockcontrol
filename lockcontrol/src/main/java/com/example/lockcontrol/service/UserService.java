package com.example.lockcontrol.service;

import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ruiruisa
 * @create 2022-05-17 20:15
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User loginUser(String name, String password){
        return userMapper.loginUser(name,password);
    }
    public void register(User user){
        userMapper.register(user);
    }

    public Date getTime(Integer id){
        return userMapper.getTime(id);
    }
}

