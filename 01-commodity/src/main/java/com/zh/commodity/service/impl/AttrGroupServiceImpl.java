package com.zh.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.zh.commodity.entity.AttrEntity;
import com.zh.commodity.service.AttrService;
import com.zh.commodity.vo.AttrGroupWithAttsrVo;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.commodity.dao.AttrGroupDao;
import com.zh.commodity.entity.AttrGroupEntity;
import com.zh.commodity.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Resource
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {

        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if( catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id",catelogId);

            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }
    }

    /**
     *根据品牌(三级分类)ID 获取属性分组和属性
     */
    @Override
    public List<AttrGroupWithAttsrVo> getAttrGroupWithAttrByCatelogId(Long catelogId) {

        //查询所有分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        //查询所有属性
        List<AttrGroupWithAttsrVo> collect = attrGroupEntities.stream().map(item -> {
            AttrGroupWithAttsrVo attrGroupWithAttsrVo = new AttrGroupWithAttsrVo();
            BeanUtils.copyProperties(item,attrGroupWithAttsrVo);

            //根据分组ID 查询属性
            List<AttrEntity> attrEntities = attrService.getRelationAttr(attrGroupWithAttsrVo.getAttrGroupId());

            attrGroupWithAttsrVo.setAttrs(attrEntities);
            return attrGroupWithAttsrVo;
        }).collect(Collectors.toList());
        return collect;
    }


}