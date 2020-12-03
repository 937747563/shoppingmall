package com.zh.service.impl;

import com.alibaba.fastjson.JSON;
import com.zh.common.to.es.SkuEsModel;
import com.zh.config.GulimallElasticSearchConfig;
import com.zh.constnt.EsConstant;
import com.zh.service.CommoditySaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class CommoditySaveServiceImpl implements CommoditySaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 上架商品
     */
    @Override
    public void commodityUp(List<SkuEsModel> skuEsModels) {

        //建立索引  建立映射关系

        //保存数据  BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();

        for (SkuEsModel model:skuEsModels){
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.COMMODITY_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s = JSON.toJSONString(model);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk=null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("06-search 商品上架功能 ES保存错误");
        }
        //TODO 若果有错处理






    }
}
