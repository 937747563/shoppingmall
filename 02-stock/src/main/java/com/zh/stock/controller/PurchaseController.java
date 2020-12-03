package com.zh.stock.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.zh.stock.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zh.stock.entity.PurchaseEntity;
import com.zh.stock.service.PurchaseService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;



/**
 * 采购信息
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 22:08:44
 */
@RestController
@RequestMapping("stock/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping("/test")
    public String test(){
        return "02-stock测试服务";
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("stock:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("stock:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("stock:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){

        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("stock:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("stock:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 采购需求——>查询采购单（未完成）
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("stock:purchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }


    /**
     *  合并采购单（把采购需求添加到采购单中）
     */
    @RequestMapping("/merge")
    //@RequiresPermissions("stock:purchase:save")
    public R saveMerge(@RequestBody MergeVo mergeVo){
        purchaseService.savePurchaseMerge(mergeVo);

        return R.ok();
    }



}
