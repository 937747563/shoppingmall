package com.zh.commodity.controller;

import java.util.Arrays;
import java.util.Map;

import com.zh.commodity.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zh.commodity.entity.SpuInfoEntity;
import com.zh.commodity.service.SpuInfoService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;



/**
 * spu信息
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
@RestController
@RequestMapping("commodity/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 查询spu信息
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.querByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("commodity:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 创建商品信息
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
        //spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(spuSaveVo);

        return R.ok();
    }

    /**
     * 商品上架
     */
    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId){

        spuInfoService.up(spuId);

        return R.ok();
    }


}
