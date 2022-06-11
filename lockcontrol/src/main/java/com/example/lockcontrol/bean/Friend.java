package com.example.lockcontrol.bean;

import lombok.Data;

/**
 * @author ruiruisa
 * @create 2022-05-28 15:58
 */
@Data
public class Friend {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    //0为M 1为S
    private String fwithUser;
    private String fwithFriend;

    private String friendName;
    private Integer yesorno;
}
