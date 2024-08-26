package com.imooc.bilibili.service.util;

import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.service.UserService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * 作者：xgp
 * 时间：2024/4/29
 * 描述：
 */

//todo
@Component
public class FastDFSUtil {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${fdfs.http.storage-addr}")
    private String httFdfsStorageAddr;

    private static final String DEFAULT_GROUP = "group1";

    private static final String PATH_KEY = "path-key:";

    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";

    private static final String UPLOADED_NO_KEY = "uploaded-no-key:";

    //文件分片大小
    private static final int SLICE_SIZE = 1024*1024*6;
    public String getFileType(MultipartFile file){
        if(file == null){
            throw new ConditionException("非法文件！");
        }

        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);

    }
    //上传文件
    public String uploadCommonFile(MultipartFile file) throws Exception {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    //上传可以断点续传的文件
    //第一片文件调用此方法，返回一个文件路径
    public String uploadAppenderFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileType = getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();

    }

    //第二片文件开始，用第一片文件返回的文件路径，调用此方法上传文件分片
    public void modifyAppendFile(MultipartFile file,String filePath,long offset) throws Exception {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP,filePath,file.getInputStream(),file.getSize(),offset);

    }

    //通过分片上传文件
    public String uploadFileBySlices(MultipartFile file,String fileMD5,Integer sliceNo,Integer totalSliceNo) throws Exception {
        if(file == null || sliceNo == null || totalSliceNo == null){
            new ConditionException("参数异常！");
        }

        //文件的存储路经
        String pathKey = PATH_KEY + fileMD5;
        //已经上传的所有分片的总大小
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMD5;
        //目前已经上传的分片数
        String uploadedNoKey = UPLOADED_NO_KEY + fileMD5;

        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if(!StringUtil.isNullOrEmpty(uploadedSizeStr)){
            uploadedSize = Long.parseLong(uploadedSizeStr);
        }

        String fileType = this.getFileType(file);

        //上传
        //如果上传的是第一个分片，调用uploadAppenderFile（）方法
        if(sliceNo == 1){
            String path = this.uploadAppenderFile(file);
            if(StringUtil.isNullOrEmpty(path)){
                throw new ConditionException("上传失败！");
            }
            //将文件的保存路径保存到redis
            //记录分片序号
            redisTemplate.opsForValue().set(pathKey,path);
            redisTemplate.opsForValue().set(uploadedNoKey,"1");
        }else{
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if(StringUtil.isNullOrEmpty(filePath)){
                throw new ConditionException("上传失败！");
            }

            this.modifyAppendFile(file,filePath,uploadedSize);
            //redis更新
            redisTemplate.opsForValue().increment(uploadedNoKey);
        }
        //生成已经上传的文件总大小，保存到redis中
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey,String.valueOf(uploadedSize));



        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        Integer uploadedNo = Integer.valueOf(uploadedNoStr);

        String resultPath = "";
        //判断文件是否上传完毕，若完毕，清空redis关于文件信息的键值对
        if(uploadedNo.equals(totalSliceNo)){
            List<String> keyList = Arrays.asList(uploadedNoKey,pathKey,uploadedSizeKey);
            resultPath = redisTemplate.opsForValue().get(pathKey);
            redisTemplate.delete(keyList);
        }


        //上传完毕前：返回"";上传完毕后：返回"pathKey"
        return resultPath;
    }


    //文件分片
    public void convertFileToSlices(MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        String fileType = this.getFileType(multipartFile);
        File file = this.mutilpartFileToFile(multipartFile);

        //文件分片
        long fileLength = file.length();
        int count = 1; //计数器
        for(int i = 0; i < fileLength; i += SLICE_SIZE){

            //支持随机访问的文件类
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            //设置文件分片起始点
            randomAccessFile.seek(i);

            //读一个文件分片大小的文件内容
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);

            //指定分片文件的存储位置
            String path = "/Users/xgp/Desktop/bilibili-temp-file/" + count + "."+fileType;

            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes,0,len);
            fos.close();
            randomAccessFile.close();
            count++;
        }

        //所有文件分片保存完毕之后，删除file
        file.delete();
    }

    public File mutilpartFileToFile(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();

        //fileName数组 = 文件名称 + 文件类型
        String[] fileName = originalFilename.split("\\.");
        File file = File.createTempFile(fileName[0],"."+fileName[1]);
        multipartFile.transferTo(file);
        return file;
    }

    //删除文件
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }


    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {

        //向文件服务器查询path对应文件的文件信息
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, path);

        //获得文件大小
        long totalFileSize = fileInfo.getFileSize();

        //拼接path，得到绝对路经
        String  url = httFdfsStorageAddr + path;

        //获取请求头所有属性
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header,request.getHeader(header));
        }

        //获取请求的文件范围
        String rangeStr =request.getHeader("Range");
        String[] range;

        if(StringUtil.isNullOrEmpty(rangeStr)){
            rangeStr = "bytes=0-" + (totalFileSize -1) ;
        }

        //如果请求的范围不是空字符串,则分割此字符串
        //通过分割，区分出起始和结尾的字节位置
        range = rangeStr.split("bytes=|-");
        long begin = 0;
        if(range.length >= 2){
            begin = Long.parseLong(range[1]);
        }
        long end = totalFileSize - 1;


        //当前端播放器请求特定片段时，end为特定片段的结尾
        if(range.length >= 3){
            end = Long.parseLong(range[2]);
        }


        //计算分片长度
        long len = (end - begin) + 1;

        String contentRange = "bytes " + begin + "-" + end + "/" + totalFileSize;
        response.setHeader("Content-Range",contentRange);
        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Content-Type","video/mp4");
        response.setContentLength((int)len);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        HttpUtil.get(url,headers,response);
    }
}
