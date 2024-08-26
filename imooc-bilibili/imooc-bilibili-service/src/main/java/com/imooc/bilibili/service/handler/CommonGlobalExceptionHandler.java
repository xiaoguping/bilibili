package com.imooc.bilibili.service.handler;

import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.domain.JsonResponse;
import com.sun.org.apache.xerces.internal.dom.AbortException;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：xgp
 * 时间：2024/4/19
 * 描述：
 */

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        String errorMsg = e.getMessage();
        if(e instanceof ClientAbortException) {
            System.out.println("ClientAbortException");
            return null;
        }
        if(e instanceof ConditionException){
            String errorCode = ((ConditionException) e).getCode();
            return new JsonResponse<>(errorCode, errorMsg);
        }else{
            return new JsonResponse<>("500",errorMsg);
        }
    }
}
