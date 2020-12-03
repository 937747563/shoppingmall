package com.zh.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.commodity.entity.SpuInfoDescEntity;
import com.zh.commodity.vo.SpuSaveVo;
import com.zh.common.utils.PageUtils;
import com.zh.commodity.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);


    PageUtils querByCondition(Map<String, Object> params);

    void up(Long spuId);
}

