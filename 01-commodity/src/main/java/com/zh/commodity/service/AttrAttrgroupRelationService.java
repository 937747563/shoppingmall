package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.AttrAttrgroupRelationEntity;

import java.util.Map;

/**
 * ????&???Է???????
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-23 17:57:20
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

