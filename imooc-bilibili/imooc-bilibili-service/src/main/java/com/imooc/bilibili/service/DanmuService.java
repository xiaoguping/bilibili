package com.imooc.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.dao.DanmuDao;
import com.imooc.bilibili.domain.Danmu;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 作者：xgp
 * 时间：2024/5/7
 * 描述：
 */

@Service
public class DanmuService {

    private static final String DANMU_KEY = "danmu-video-";

    @Autowired
    private DanmuDao danmuDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void addDanmu(Danmu danmu) {
        danmuDao.addDanmu(danmu);
    }

    //异步调用
    @Async
    public void asyncAddDanmu(Danmu danmu) {
        danmuDao.addDanmu(danmu);
    }

    public List<Danmu> getDanmus(Map<String,Object> params) {
        return danmuDao.getDanmu(params);
    }

    public void addDanmusToRedis(Danmu danmu) {
        String key = DANMU_KEY + danmu.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();

        if(!StringUtil.isNullOrEmpty(value)){
            //String转换成对象
            list = JSONArray.parseArray(value, Danmu.class);
        }
        list.add(danmu);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));

    }

    public List<Danmu> getDanmus(Long videoId, String startTime, String endTime)throws Exception{
        String key = DANMU_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list;
        if(!StringUtil.isNullOrEmpty(value) && !value.equals("[]")){
            list = JSONArray.parseArray(value, Danmu.class);
            if(!StringUtil.isNullOrEmpty(startTime) && !StringUtil.isNullOrEmpty(endTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Danmu> childList = new ArrayList<>();
                for(Danmu danmu : list){
                    Date createTime = danmu.getCreateTime();
                    if(createTime.after(startDate) && createTime.before(endDate)){
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
        }else{
            Map<String,Object> params = new HashMap<>();
            params.put("videoId", videoId);
            params.put("startTime", null);
            params.put("endTime", null);
            list = danmuDao.getDanmu(params);

            //保存弹幕到redis
            redisTemplate.opsForValue().set(key,JSONObject.toJSONString(list));
        }

        return list;
    }
}
