package com.imooc.bilibili.domain;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */
public class JsonResponse<T> {

    private String code;
    private String msg;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data) {
        code = "0";
        msg = "成功";
        this.data = data;
    }

    public static JsonResponse<String> success(){
        return new JsonResponse<>(null);
    }

    public static JsonResponse<String> success(String data){
        return new JsonResponse<>(data);
    }

    public static JsonResponse<String> fail(){
        return new JsonResponse<>("1","失败");
    }

    public static JsonResponse<String> fail(String code,String msg){
        return new JsonResponse<>(code,msg);
    }



}
