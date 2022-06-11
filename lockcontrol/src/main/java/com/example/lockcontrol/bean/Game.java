package com.example.lockcontrol.bean;

import lombok.Data;

import java.io.File;
import java.util.Date;
/**
 * @author ruiruisa
 * @create 2022-05-15 11:37
 */
@Data
public class Game {
    private int id;
    private Date datetime;
    private int clean;
    private Integer photo;
    private int userId;
    private int openorclose;
    private int gameType;
    private int masterId;
    private String task;

    //0:未完成 1:完成
    private int tasksuccess;

    private int teletask;
}
