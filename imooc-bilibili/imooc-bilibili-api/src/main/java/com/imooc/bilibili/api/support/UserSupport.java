package com.imooc.bilibili.api.support;

import com.imooc.bilibili.domain.Exception.ConditionException;
import com.imooc.bilibili.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者：xgp
 * 时间：2024/4/20
 * 描述：
 */

@Component
public class UserSupport {

    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);

        if(userId < 0){
            throw new ConditionException("非法用户！");
        }

        return userId;
    }


}
