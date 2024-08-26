package com.imooc.bilibili.api;

import com.imooc.bilibili.api.support.UserSupport;
import com.imooc.bilibili.domain.Danmu;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.service.DanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 作者：xgp
 * 时间：2024/5/8
 * 描述：
 */

@RestController
public class DanmuApi {

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private UserSupport userSupport;

    @GetMapping("/danmus")
    public JsonResponse<List<Danmu>> getDanmus(@RequestParam Long videoId,
                                               String startTime,
                                               String endTime)throws Exception {
        List<Danmu> list;
        try{
            userSupport.getCurrentUserId();
            //用户模式可以筛选弹幕
            list = danmuService.getDanmus(videoId,startTime,endTime);
        }catch (Exception e){
            //游客模式
            list = danmuService.getDanmus(videoId,null,null);
        }

        return new JsonResponse<>(list);
    }
}
