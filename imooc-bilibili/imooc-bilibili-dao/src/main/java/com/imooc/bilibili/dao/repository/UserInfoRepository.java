package com.imooc.bilibili.dao.repository;

import com.imooc.bilibili.domain.UserInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 作者：xgp
 * 时间：2024/8/16
 * 描述：
 */
public interface UserInfoRepository extends ElasticsearchRepository<UserInfo,Long> {

}
