package com.zh.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.common.utils.PageUtils;
import com.zh.stock.entity.PurchaseEntity;
import com.zh.stock.vo.MergeVo;

import java.util.Map;

/**
 * 采购信息
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void savePurchaseMerge(MergeVo mergeVo);

    void updateStatusById(Long purchaseId);
}

