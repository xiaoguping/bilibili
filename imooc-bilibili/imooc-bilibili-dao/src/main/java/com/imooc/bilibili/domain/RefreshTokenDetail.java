package com.imooc.bilibili.domain;

import java.util.Date;

/**
 * 作者：xgp
 * 时间：2024/4/26
 * 描述：
 */
public class RefreshTokenDetail {

    private Long id;
    private String refreshToken;
    private Long userId;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
