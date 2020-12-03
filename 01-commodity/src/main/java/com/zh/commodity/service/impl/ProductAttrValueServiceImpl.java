package com.zh.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.commodity.dao.ProductAttrValueDao;
import com.zh.commodity.entity.ProductAttrValueEntity;
import com.zh.commodity.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 创建商品——>保存spu规格参数
     */
    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }



    /**
     * 根据spuId查询其参数信息
     */
    @Override
    public List<ProductAttrValueEntity> queryBySpuId(Long spuId) {

        List<ProductAttrValueEntity> entityList = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return entityList;
    }

    /**
     * 根据spuId 修改spu的Att
     */
    @Transactional
    @Override
    public void updateSpuAttrBySpuId(Long spuId, List<ProductAttrValueEntity> entityList) {

        //不知道以前是否添加过SpuAttr 是新增还是修改 因此需要 删除SpuAttr 在新增
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        List<ProductAttrValueEntity> collect = entityList.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());

        this.saveBatch(collect);
    }



}