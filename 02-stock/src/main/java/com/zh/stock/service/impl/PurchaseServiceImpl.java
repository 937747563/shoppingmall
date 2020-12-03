package com.zh.stock.service.impl;

import com.zh.stock.entity.PurchaseDetailEntity;
import com.zh.stock.entity.PurchasePurchaseDetailRelationalEntity;
import com.zh.stock.service.PurchaseDetailService;
import com.zh.stock.service.PurchasePurchaseDetailRelationalService;
import com.zh.stock.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.stock.dao.PurchaseDao;
import com.zh.stock.entity.PurchaseEntity;
import com.zh.stock.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private PurchasePurchaseDetailRelationalService purchasePurchaseDetailRelationalService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询采购单
     */
    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1));

        return new PageUtils(page);
    }

    /**
     * 合并采购单（将采购需求添加到采购单中）
     */
    @Transactional
    @Override
    public void savePurchaseMerge(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();  //采购单Id

        //未选择采购单 需要新建采购单
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setPriority(1);
            purchaseEntity.setStatus(1);
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);

            purchaseId=purchaseEntity.getId();
        }
        List<Long> items = mergeVo.getItems();

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(1);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        //修改采购单更新时间
        PurchaseEntity purchaseEntity=new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

        //建立采购单与采购需求的连接
        List<PurchasePurchaseDetailRelationalEntity> purchaseDetailRelationalEntityList = items.stream().map(item -> {
            PurchasePurchaseDetailRelationalEntity purchasePurchaseDetailRelationalEntity = new PurchasePurchaseDetailRelationalEntity();
            purchasePurchaseDetailRelationalEntity.setPurchaseId(finalPurchaseId);
            purchasePurchaseDetailRelationalEntity.setPurchaseDetailId(item);
            return purchasePurchaseDetailRelationalEntity;
        }).collect(Collectors.toList());
        purchasePurchaseDetailRelationalService.saveBatch(purchaseDetailRelationalEntityList);

    }

    /**
     * 完成订单
     */
    @Override
    public void updateStatusById(Long purchaseId) {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setStatus(3);
        this.updateById(purchaseEntity);
    }


}