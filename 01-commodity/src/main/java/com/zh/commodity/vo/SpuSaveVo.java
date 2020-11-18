/**
 * Copyright 2019 bejson.com
 */
package com.zh.commodity.vo;

import com.zh.commodity.vo.spuSaveVoUtil.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class SpuSaveVo {

    //商品名称
    private String spuName;
    //描述
    private String spuDescription;
    //三级分类ID
    private Long catalogId;
    //品牌ID
    private Long brandId;
    //商品重量
    private BigDecimal weight;
    //上架状态
    private int publishStatus;
    //商品介绍图片
    private List<String> decript;
    //商品图集照片地址
    private List<String> images;
    //金币、成长值
    private Bounds bounds;
    //商品属性
    private List<BaseAttrs> baseAttrs;
    //sku信息
    private List<Skus> skus;



}