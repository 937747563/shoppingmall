package com.zh.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.common.utils.PageUtils;
import com.zh.stock.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void finish(Long[] ids);
}

