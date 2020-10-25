package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.CategoryEntity;

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
}

