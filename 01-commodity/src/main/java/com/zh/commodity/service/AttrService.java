package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.commodity.vo.AttrGroupRelationVo;
import com.zh.commodity.vo.AttrRespVo;
import com.zh.commodity.vo.AttrVo;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attrVo);

    PageUtils querySaleAttrPage(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    void deleteRelation(AttrGroupRelationVo[] attrGroupRelationVo);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrGroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}


