package com.imooc.bilibili.service;

import com.imooc.bilibili.dao.repository.UserInfoRepository;
import com.imooc.bilibili.dao.repository.VideoRepository;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.Video;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 作者：xgp
 * 时间：2024/8/16
 * 描述：
 */

@Service
public class ElasticSearchService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void addVideo(Video video) {
        //数据添加到es中
        videoRepository.save(video);
    }

    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public Video getVideos(String keyword) {

        //根据关键词模糊查询
        Video video = videoRepository.findByTitleLike(keyword);
        return video;
    }


    //全文搜索
    public List<Map<String, Object>> getContents(String keyword,
                                                 Integer pageNo,Integer pageSize) throws IOException {
        String[] indices = {"videos","user-infos"};
        SearchRequest searchRequest = new SearchRequest(indices);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //设置分页
        searchSourceBuilder.from(pageNo -1);
        searchSourceBuilder.size(pageSize);

        //设置多条件查询，设置查询的字段
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(keyword,"title",
                "nick","description");

        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);


        //设置查询超时
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮显示(keyword部分高亮)
        //若keyword与字段匹配，则高亮这些匹配的字段
        String[] array = {"title", "nick","description"};

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for(String key:array){
            //添加高亮字段
            highlightBuilder.fields().add(new HighlightBuilder.Field(key));
        }

        //如果要多个字段进行高亮，这里要设置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");//设置高亮的形式
        highlightBuilder.postTags("</span>");

        searchSourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String, Object>> list = new ArrayList<>();

        //遍历查询到的内容（击中的内容）
        for(SearchHit hit:searchResponse.getHits()){
            //获取高亮字段
            Map<String, HighlightField> highLightBuilderFields = hit.getHighlightFields();

            //获取查询的内容
            Map<String,Object> sourceMap = hit.getSourceAsMap();


            //将高亮的内容替换进查询结果
            for(String key: array){
                HighlightField highlightField = highLightBuilderFields.get(key);
                if(highlightField != null){
                    Text[] fragments = highlightField.fragments();
                    String str = Arrays.toString(fragments);
                    str = str.substring(1, str.length() - 1);
                    sourceMap.put(key,str);
                }
            }

            list.add(sourceMap);
        }
        return list;
    }


    public void deleteVideos() {
        videoRepository.deleteAll();
    }


}
