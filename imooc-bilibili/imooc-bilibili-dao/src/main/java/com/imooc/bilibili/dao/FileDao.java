package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作者：xgp
 * 时间：2024/5/2
 * 描述：
 */

@Mapper
public interface FileDao {
    Integer addFile(File file);
    com.imooc.bilibili.domain.File getFileByMD5(String md5);
}
