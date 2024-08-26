package com.imooc.bilibili.dao.repository;

import com.imooc.bilibili.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 作者：xgp
 * 时间：2024/8/16
 * 描述：
 */

//继承ElasticsearchRepository相当于起到@Mapper注解的功能
public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    //find by title like (springdata自动识别)
    Video findByTitleLike(String keyword);
}
