package com.example.lockcontrol.bean;

import lombok.Data;

/**
 * @author ruiruisa
 * @create 2022-05-21 16:56
 */
@Data
public class NewGame {
    private String type;
    private Integer month;
    private Integer day;
    private Integer hour;
    private String clean;
    private Integer cleannum;
    private String link;
}
