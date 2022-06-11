package com.example.lockcontrol.service;

import com.example.lockcontrol.bean.Game;
import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.mapper.GameMapper;
import com.example.lockcontrol.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

/**
 * @author ruiruisa
 * @create 2022-05-15 11:50
 */
@Service
public class GameService {
    @Autowired
    private GameMapper gameMapper;

    public void toNewGame(Game game){
        gameMapper.toNewGame(game);
    }

    public void masterToNewGame(Game game){
        gameMapper.masterToNewGame(game);
    }

    public void setPhoto(Integer num, Integer id){
        gameMapper.setPhoto(num,id);
    }

    public Game findGameUser(Integer id){
        return gameMapper.findGameUser(id);
    }

    public Game findGameMaster(Integer id){
        return gameMapper.findGameMaster(id);
    }

    public Integer findClean(Integer id){
        return gameMapper.findClean(id);
    }

    public void cutClean(Integer cleannum, Integer id){
        gameMapper.cutClean(cleannum, id);
    }

    public Integer getPhoto(Integer id){
        return gameMapper.getPhoto(id);
    }

    public void delGame(Integer id){
        gameMapper.delGame(id);
    }

    public Integer findOpen(Integer id){
        return gameMapper.findOpen(id);
    }
    public void lockOn(Integer open,Integer id){
        gameMapper.lockOn(open, id);
    }

    public void addTimeForOtherUsers(Date time, Integer id){
        gameMapper.addTimeForOtherUsers(time, id);
    }

    public List<Game> findMasterGame(Integer id){
        return gameMapper.findMasterGame(id);
    }

    public String findName(Integer id){
        return gameMapper.findName(id);
    }

    public void addTask(Integer id,String text){
        gameMapper.addTask(id, text);
    }

    public Game findGameId(Integer id){
        return gameMapper.findGameId(id);
    }

    public String findTask(Integer id){
        return gameMapper.findTask(id);
    }

    public void delTask(Integer id){
        gameMapper.delTask(id);
    }

    public void setTaskTel(Integer id){
        gameMapper.setTaskTel(id);
    }

    public List<Game> findTelGame(){
        return gameMapper.findTelGame();
    }

    public void masterToTask(Integer id){
        gameMapper.masterToTask(id);
    }
}

