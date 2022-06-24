package com.example.lockcontrol.controller;

import com.example.lockcontrol.bean.User;
import com.example.lockcontrol.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.lockcontrol.oss.OSSClient;
import org.springframework.web.multipart.MultipartFile;

import com.example.lockcontrol.oss.OSSClient;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLConnection;

/**
 * @author ruiruisa
 * @create 2022-06-23 22:05
 */
@Controller
public class FileController {
    @Autowired
    private FileService fileService;


    @PostMapping("/file")
    public String file(MultipartFile photo, HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        File file = transferToFile(photo);
        String name = photo.getOriginalFilename();
        String url = "exampledir/" + name;
        OSSClient.ossupdate(file,url);
        //将文件路径存到数据库中

        
        return "redirect:/home";
    }

    private File transferToFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file=File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
