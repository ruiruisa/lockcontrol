package com.example.lockcontrol.mapper;

import com.example.lockcontrol.bean.Game;
import com.example.lockcontrol.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.Mapping;

import java.util.Date;
import java.util.List;

/**
 * @author ruiruisa
 * @create 2022-05-15 11:50
 */
@Mapper
public interface GameMapper {
    //随机密码
    public void toNewGame(Game game);

    //管理者
    public void masterToNewGame(Game game);

    public void setPhoto(Integer num, Integer id);

    //查询是否有游戏user_id
    public Game findGameUser(Integer id);

    //查询是否有游戏master_id
    public Game findGameMaster(Integer id);

    //查询游戏id
    public Game findGameId(Integer id);

    //查询剩余清洁次数
    public Integer findClean(Integer id);

    //减少一次清洁次数
    public void cutClean(Integer cleannum, Integer id);

    //查询图片名称
    public Integer getPhoto(Integer id);

    //删除游戏
    public void delGame(Integer id);

    //查询撬锁次数
    public Integer findOpen(Integer id);

    //设置撬锁次数
    public void lockOn(Integer open,Integer id);

    //其他用户添加时间(1h)
    public void addTimeForOtherUsers(Date time, Integer id);

    //查找奴
    public List<Game> findMasterGame(Integer id);

    //查找姓名
    public String findName(Integer id);

    //添加任务
    public void addTask(Integer id,String text);

    //设置任务为Tel
    public void setTaskTel(Integer id);

    //查询任务
    public String findTask(Integer id);

    //删除任务
    public void delTask(Integer id);

    //查询Tel的任务
    public List<Game> findTelGame();

    //管理者提交任务
    public void masterToTask(Integer id);
}
