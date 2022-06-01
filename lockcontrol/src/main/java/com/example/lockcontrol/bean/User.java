package com.example.lockcontrol.bean;

import lombok.Data;

/**
 * @author ruiruisa
 * @create 2022-05-17 19:13
 */
@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    //正版
    private int grade;

    private int open;
    private char attribute;
}
