package com.zh.commodity.dao;

import com.zh.commodity.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
