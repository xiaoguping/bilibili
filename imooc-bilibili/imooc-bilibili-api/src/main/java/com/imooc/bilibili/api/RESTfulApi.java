package com.imooc.bilibili.api;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */
@RestController
public class RESTfulApi {
    private final Map<Integer, Map<String,Object>> dataMap;

    public RESTfulApi() {
        dataMap = new HashMap<>();
        for(int i = 1; i < 3; i++){
            Map<String,Object> data = new HashMap<>();
            data.put("id",i);
            data.put("name","name"+1);
            dataMap.put(i,data);
        }

    }

    @GetMapping("/objects/{id}")
    public Map<String,Object> getData(@PathVariable Integer id){
        return dataMap.get(id);
    }

    @DeleteMapping("/objects/{id}")
    public String deleData(@PathVariable Integer id){
        dataMap.remove(id);
        return "success";
    }

    @PostMapping("/objects")
    public String postData(@RequestBody Map<String,Object> data){
        Integer[] array = dataMap.keySet().toArray(new Integer[0]);
        Arrays.sort(array);
        int nextId = array[array.length-1] + 1;
        dataMap.put(nextId,data);

        return "post success";
    }

    @PutMapping("/objects")
    public String putData(@RequestBody Map<String,Object> data){
        int curId = Integer.valueOf(String.valueOf(data.get("id")));
        if(dataMap.containsKey(curId)){
            //更新操作
            dataMap.put(curId,data);

        }else{
            //新增操作
            Integer[] array = dataMap.keySet().toArray(new Integer[0]);
            Arrays.sort(array);
            int nextId = array[array.length-1] + 1;
            dataMap.put(nextId,data);
        }

        return "put success";
    }
}
