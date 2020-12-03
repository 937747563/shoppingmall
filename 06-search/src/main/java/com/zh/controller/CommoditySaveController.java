package com.zh.controller;

import com.zh.common.to.es.SkuEsModel;
import com.zh.common.utils.R;
import com.zh.service.CommoditySaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/search/save")
@RestController
@Slf4j
public class CommoditySaveController {

    @Autowired
    private CommoditySaveService commoditySaveService;

    /**
     * 上架商品
     */
    @PostMapping("/commodity/up")
    public R commodityUp(@RequestBody List<SkuEsModel> skuEsModels){

        log.info("商品上架功能调用");
        log.info("保存的ES对象"+skuEsModels);
        commoditySaveService.commodityUp(skuEsModels);

        return R.ok();
    }

}
