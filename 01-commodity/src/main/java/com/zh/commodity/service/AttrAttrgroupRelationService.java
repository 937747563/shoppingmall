package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.commodity.vo.AttrGroupRelationVo;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(List<AttrGroupRelationVo> relationVos);
}

