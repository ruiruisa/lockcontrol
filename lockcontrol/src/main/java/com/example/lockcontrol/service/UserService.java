package com.example.lockcontrol.service;

import com.example.lockcontrol.bean.Friend;
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

    public User checkEmail(String email){
        return userMapper.checkEmail(email);
    }


    public Date getTime(Integer id){
        return userMapper.getTime(id);
    }
    public List<Friend> getFriend(Integer id){
        return userMapper.getFriend(id);
    }


    public List<User> getUser(String name){
        return userMapper.getUser(name);
    }

    public void addFriend(Friend friend){
        userMapper.addFriend(friend);
    }

    public User findUser(Integer id){
        return userMapper.findUser(id);
    }

    public void yesFriends(Integer userId,Integer friendId){
        userMapper.yesFriends(userId,friendId);
    }

    public void noFriends(Integer userId,Integer friendId){
        userMapper.noFriends(userId, friendId);
    }

    public void attributeSet(Integer id, Integer fwithFriend,Integer fwithUser){
        userMapper.attributeSet(id, fwithFriend, fwithUser);
    }


    public Date getNTime(Integer id){
        return userMapper.getNTime(id);
    }

    public void masterLockOn(Integer gameId){
        userMapper.masterLockOn(gameId);
    }

    public Friend findFriendMessage(Integer id){
        return userMapper.findFriendMessage(id);
    }

}

