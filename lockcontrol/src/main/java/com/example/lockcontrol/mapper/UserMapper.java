package com.example.lockcontrol.mapper;

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
}
