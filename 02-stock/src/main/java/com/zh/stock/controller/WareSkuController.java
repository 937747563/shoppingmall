package com.zh.stock.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zh.stock.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zh.stock.entity.WareSkuEntity;
import com.zh.stock.service.WareSkuService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;



/**
 * 商品库存
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
@RestController
@RequestMapping("stock/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * （商品库存）查询
     */
    @RequestMapping("/list")
    //@RequiresPermissions("stock:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("stock:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("stock:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("stock:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("stock:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询sku是否有库存
     */
    @PostMapping("/hasstock")
    public R<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds){

        List<SkuHasStockVo> stockVos= wareSkuService.getSkusHasStock(skuIds);

        R<List<SkuHasStockVo>> ok = R.ok();
        ok.setData(stockVos);
        return ok;
    }

}
