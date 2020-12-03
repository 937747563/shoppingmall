package com.zh.stock.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zh.stock.entity.PurchaseDetailEntity;
import com.zh.stock.service.PurchaseDetailService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;



/**
 * 
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
@RestController
@RequestMapping("stock/purchasedetail")
public class PurchaseDetailController {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("stock:purchasedetail:info")
    public R info(@PathVariable("id") Long id){
		PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

        return R.ok().put("purchaseDetail", purchaseDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("stock:purchasedetail:save")
    public R save(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.save(purchaseDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("stock:purchasedetail:update")
    public R update(@RequestBody PurchaseDetailEntity purchaseDetail){
		purchaseDetailService.updateById(purchaseDetail);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("stock:purchasedetail:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 采购需求查询
     */
    @RequestMapping("/list")
    //@RequiresPermissions("stock:purchasedetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseDetailService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 完成采购单
     */
    @RequestMapping("/finish")
    public R finish(@RequestBody Long[] purchaseId){

        purchaseDetailService.finish(purchaseId);

        return R.ok();
    }




}
