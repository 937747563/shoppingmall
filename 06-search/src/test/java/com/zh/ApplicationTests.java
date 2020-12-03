package com.zh;

import com.alibaba.fastjson.JSON;
import com.zh.config.GulimallElasticSearchConfig;
import com.zh.entity.User;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RestHighLevelClient client;


    @Test
    public void tset(){
        System.out.println(client);
    }


    /**
     * 保存更新数据
     */
    @Test
    public void index() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");   //数据的id

        // indexRequest.source("userName","zhangsan","age",18,"gender","男");

        User user = new User();
        user.setName("zs");
        user.setAge(18);
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);  //要保存的内容

        //执行操作
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //提取有用的响应数据
        System.out.println(index);

    }


    /**
     * 查询信息
     */
    @Test
    public void search() throws IOException {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");

        //检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));


        searchRequest.source(searchSourceBuilder);

        //执行检索
        SearchResponse search = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(searchSourceBuilder);
        System.out.println(search);
    }



}
