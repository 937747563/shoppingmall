package com.zh.stock.service.impl;

import com.zh.stock.entity.PurchasePurchaseDetailRelationalEntity;
import com.zh.stock.entity.WareSkuEntity;
import com.zh.stock.service.PurchaseService;
import com.zh.stock.service.WareSkuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.stock.dao.PurchaseDetailDao;
import com.zh.stock.entity.PurchaseDetailEntity;
import com.zh.stock.service.PurchaseDetailService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchasePurchaseDetailRelationalServiceImpl purchaseDetailRelationalService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;



    /**
     * 采购需求查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");

        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(w->{
                w.eq("id",key)
                        .or().eq("purchase_id",key)
                        .or().eq("sku_id",key);
            });
        }
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),queryWrapper);

        return new PageUtils(page);
    }

    /**
     * 完成采购单
     */
    @Transactional
    @Override
    public void finish(Long[] id) {

        Long purchaseId = Integer.valueOf(Math.toIntExact(id[0])).longValue() ;

        //更改采购单状态
        purchaseService.updateStatusById(purchaseId);

        //修改采购需求状态
        List<PurchasePurchaseDetailRelationalEntity> purchaseDetailRelationalEntities = purchaseDetailRelationalService.list(new QueryWrapper<PurchasePurchaseDetailRelationalEntity>().eq("purchase_id", purchaseId));
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailRelationalEntities.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getPurchaseDetailId());
            purchaseDetailEntity.setStatus(3);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

        //添加库存
        List<Long> idList = purchaseDetailEntityList.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        Collection<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(idList);

        List<WareSkuEntity> wareSkuEntityList = purchaseDetailEntities.stream().map(item -> {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            BeanUtils.copyProperties(item, wareSkuEntity);
            wareSkuEntity.setStock(item.getSkuNum());
            return wareSkuEntity;
        }).collect(Collectors.toList());
        wareSkuService.saveBatch(wareSkuEntityList);


    }

}