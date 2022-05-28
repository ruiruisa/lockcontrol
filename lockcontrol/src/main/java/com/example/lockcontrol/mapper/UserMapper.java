package com.example.lockcontrol.mapper;

import com.example.lockcontrol.bean.Friend;
import com.example.lockcontrol.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author ruiruisa
 * @create 2022-05-17 20:13
 */
@Mapper
public interface UserMapper {
    public User loginUser(String email, String password);

    public void register(User user);

    //获取时间
    public Date getTime(Integer id);

    //获取好友id和关系
    public List<Friend> getFriend(Integer id);

    //查找用户
    public List<User> getUser(String name);

    //添加好友
    public void addFriend(Friend friend);

    //获取用户
    public User findUser(Integer id);

    //同意添加
    public void yesFriends(Integer userId,Integer friendId);

    //拒绝添加
    public void noFriends(Integer userId,Integer friendId);

}
