package com.example.lockcontrol.service;

import com.example.lockcontrol.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ruiruisa
 * @create 2022-06-23 22:25
 */
@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;

    public void addPhoto(String url,Integer id){
        fileMapper.addPhoto(url,id);
    }
}
