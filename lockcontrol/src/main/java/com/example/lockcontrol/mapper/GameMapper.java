package com.example.lockcontrol.mapper;

import com.example.lockcontrol.bean.Game;
import com.example.lockcontrol.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.Mapping;

/**
 * @author ruiruisa
 * @create 2022-05-15 11:50
 */
@Mapper
public interface GameMapper {
    public void toNewGame(Game game);

    public void setPhoto(Integer num, Integer id);

    //查询是否有游戏
    public Game findGame(Integer id);

    //查询剩余清洁次数
    public Integer findClean(Integer id);
    //减少一次清洁次数
    public void cutClean(Integer cleannum, Integer id);

    //查询图片名称
    public Integer getPhoto(Integer id);

    //删除游戏
    public void delGame(Integer id);

}