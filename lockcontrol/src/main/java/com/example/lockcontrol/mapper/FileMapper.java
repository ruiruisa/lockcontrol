package com.example.lockcontrol.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author ruiruisa
 * @create 2022-06-23 22:25
 */
@Mapper
public interface FileMapper {
    public void addPhoto(String url,Integer id);
}
