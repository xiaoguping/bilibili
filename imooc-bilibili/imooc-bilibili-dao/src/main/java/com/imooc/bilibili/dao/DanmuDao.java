package com.imooc.bilibili.dao;

import com.imooc.bilibili.domain.Danmu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/5/7
 * 描述：
 */

@Mapper
public interface DanmuDao {
    Integer addDanmu(Danmu danmu);

    List<Danmu> getDanmu(Map<String,Object> params);
}
