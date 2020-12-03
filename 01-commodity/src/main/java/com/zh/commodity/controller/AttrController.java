package com.zh.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zh.commodity.entity.ProductAttrValueEntity;
import com.zh.commodity.service.ProductAttrValueService;
import com.zh.commodity.vo.AttrRespVo;
import com.zh.commodity.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zh.commodity.entity.AttrEntity;
import com.zh.commodity.service.AttrService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
@RestController
@RequestMapping("commodity/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询规格参数
     */
    @RequestMapping("/base/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId){

        PageUtils page =attrService.queryBaseAttrPage(params,catelogId);

        return R.ok().put("page", page);
    }
    /**
     * 查询销售属性
     */
    @RequestMapping("/sale/list/{catelogId}")
    public R saleAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId){

        PageUtils page =attrService.querySaleAttrPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 修改规格参数
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("commodity:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);

        AttrRespVo attrRespVo =attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVo);
    }


    /**
     * 新增规格参数
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:attr:save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.saveAttr(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:attr:update")
    public R update(@RequestBody AttrVo attrVo){
		attrService.updateAttr(attrVo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

    /**
     * spu管理——>规格维护（查询spu所包含的规格参数）
     */
    @RequestMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable Long spuId){

        //根据spuId查询其参数信息
        List<ProductAttrValueEntity> productAttrValueEntityList=productAttrValueService.queryBySpuId(spuId);
        return R.ok().put("data",productAttrValueEntityList);
    }

    //  /update/28
    /**
     * 规格维护修改spu参数信息
     */
    @RequestMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entityList){

        //根据spuId 修改spu的Att
        productAttrValueService.updateSpuAttrBySpuId(spuId,entityList);

        return R.ok();
    }


}
