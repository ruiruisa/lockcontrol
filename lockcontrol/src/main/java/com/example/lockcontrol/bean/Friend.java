package com.example.lockcontrol.bean;

import lombok.Data;

/**
 * @author ruiruisa
 * @create 2022-05-28 15:58
 */
@Data
public class Friend {
    private Integer userId;
    private Integer friendId;
    private String fwith;
    private String friendName;
    private Integer yesorno;
}
