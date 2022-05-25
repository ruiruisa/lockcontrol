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
    private int num;
}
