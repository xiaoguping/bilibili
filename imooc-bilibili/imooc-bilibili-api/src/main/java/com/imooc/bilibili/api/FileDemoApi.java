package com.imooc.bilibili.api;

import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 作者：xgp
 * 时间：2024/4/18
 * 描述：
 */

@RestController
public class FileDemoApi {

    @Autowired
    private FastDFSUtil fastDFSUtil;


    @Autowired
    private UserService userService;


    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception {
        fastDFSUtil.convertFileToSlices(file);
    }








}
