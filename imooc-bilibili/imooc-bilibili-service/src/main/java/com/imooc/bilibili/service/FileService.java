package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.FileDao;
import com.imooc.bilibili.domain.File;
import com.imooc.bilibili.service.util.FastDFSUtil;
import com.imooc.bilibili.service.util.MD5Util;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
 * 作者：xgp
 * 时间：2024/4/29
 * 描述：
 */

@Service
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;
    public String uploadFileBySlices(MultipartFile slice, String fileMD5, Integer sliceNo, Integer totalSliceNo) throws Exception {

        File dbFileByMD5 = fileDao.getFileByMD5(fileMD5);
        if(dbFileByMD5 != null) {
            return dbFileByMD5.getUrl();
        }


        String url = fastDFSUtil.uploadFileBySlices(slice, fileMD5, sliceNo, totalSliceNo);
        if(!StringUtil.isNullOrEmpty(url)){
            dbFileByMD5 = new File();
            dbFileByMD5.setCreateTime(new Date());
            dbFileByMD5.setMd5(fileMD5);
            dbFileByMD5.setUrl(url);
            dbFileByMD5.setType(fastDFSUtil.getFileType(slice));
            fileDao.addFile(dbFileByMD5);
        }

        return url;
    }

    public String getFileMD5(MultipartFile file) throws Exception {
        return MD5Util.getFileMD5(file);
    }
}
