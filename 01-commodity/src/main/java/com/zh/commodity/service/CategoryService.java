package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.commodity.vo.Catelog2Vo;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * ??Ʒ???????
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-23 17:57:20
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     * 找到 catelogId 的完成路径
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * 前台：查找一级分类
     */
    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

