package com.imooc.bilibili.domain.Exception;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */


public class ConditionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    public ConditionException(String code,String name){
        super(name);
        this.code = code;
    }

    public ConditionException(String name){
        super(name);
        code = "500";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
