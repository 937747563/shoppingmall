package com.zh.stock.dao;

import com.zh.stock.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
