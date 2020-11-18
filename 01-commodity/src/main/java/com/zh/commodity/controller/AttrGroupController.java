package com.zh.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zh.commodity.dao.AttrAttrgroupRelationDao;
import com.zh.commodity.entity.AttrEntity;
import com.zh.commodity.service.AttrAttrgroupRelationService;
import com.zh.commodity.service.AttrService;
import com.zh.commodity.service.CategoryService;
import com.zh.commodity.vo.AttrGroupRelationVo;
import com.zh.commodity.vo.AttrGroupWithAttsrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zh.commodity.entity.AttrGroupEntity;
import com.zh.commodity.service.AttrGroupService;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-30 10:07:05
 */
@RestController
@RequestMapping("commodity/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("commodity:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        System.out.println(11);
        return R.ok().put("page", page);

    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("commodity:attrgroup:list")
    public R myList(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);

        PageUtils page = attrGroupService.queryPage(params,catelogId);


        return R.ok().put("page", page);
    }


    /**
     * 属性查找关联关系
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){

        List<AttrEntity> attrEntities=attrService.getRelationAttr(attrGroupId);

        return R.ok().put("data",attrEntities);
    }
    /**
     * 查询未关联属性
     */
    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(@RequestParam Map<String, Object> params,
                            @PathVariable("attrGroupId") Long attrGroupId){

        PageUtils page=attrService.getNoRelationAttr(params,attrGroupId);
        return R.ok().put("page",page);
    }

    /**
     * 新增属性关联信息
     * /attrgroup/attr/relation
     */
    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> relationVos){
        attrAttrgroupRelationService.saveBatch(relationVos);
        return R.ok();
    }


    /**
     * 删除属性相关联信息
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] attrGroupRelationVo){

        attrService.deleteRelation(attrGroupRelationVo);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("commodity:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] path=categoryService.findCatelogPath(catelogId);

        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("commodity:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("commodity:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("commodity:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }


    /**
     * 根据品牌(三级分类)ID 获取属性分组和属性
     */
    @RequestMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttr(@PathVariable("catelogId") Long catelogId){

        //查 当前分类下所有属性分组
        //查 每个属性分组的所有属性
        List<AttrGroupWithAttsrVo> attrGroupWithAttsrVos=attrGroupService.getAttrGroupWithAttrByCatelogId(catelogId);

        return R.ok().put("data",attrGroupWithAttsrVos);
    }

}
