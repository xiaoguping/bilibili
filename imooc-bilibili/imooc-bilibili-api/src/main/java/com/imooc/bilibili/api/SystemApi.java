package com.imooc.bilibili.api;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/8/16
 * 描述：
 */

@RestController
public class SystemApi {

    @Autowired
    private ElasticSearchService elasticSearchService;


    //获取全文搜索内容
    @GetMapping("/contents")
    public JsonResponse<List<Map<String, Object>>> getContents(@RequestParam String keyword,
                                                               @RequestParam int no,
                                                               @RequestParam int size) throws IOException {
        List<Map<String, Object>> contentsList = elasticSearchService.getContents(keyword, no, size);
        return new JsonResponse<>(contentsList);
    }
}


