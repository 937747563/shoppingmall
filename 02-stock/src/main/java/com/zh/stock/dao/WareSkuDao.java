package com.zh.stock.dao;

import com.zh.stock.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Integer getSkuStock(@Param("skuId") Long skuId);
}
