package com.zh.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.commodity.dao.CategoryDao;
import com.zh.commodity.entity.CategoryEntity;
import com.zh.commodity.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 查找父子目录
     */
    @Override
    public List<CategoryEntity> listWithTree() {

        List<CategoryEntity> levelMenus=new ArrayList<>();

        List<CategoryEntity> entities=baseMapper.selectList(null);

        for(CategoryEntity categoryEntity:entities){
            if (categoryEntity.getCatLevel()==1){
                 getChildren(categoryEntity,entities);
                 levelMenus.add(categoryEntity);
            }
        }

        return levelMenus;
    }

    /**
     * 删除目录
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {

        // TODO 1、检查当前菜单是否被引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);


    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths=new ArrayList<>();

        List<Long> parentPath=findParentPath(catelogId,paths);

        Collections.reverse(parentPath);

        return parentPath.toArray(new Long[parentPath.size()]);
    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths){
        paths.add(catelogId);
        CategoryEntity byId=this.getById(catelogId);
        if (byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }


    /**
     * 添加子目录
     */
    public void getChildren(CategoryEntity categoryEntity,List<CategoryEntity> categoryEntityList){

        List<CategoryEntity> childrenList=new ArrayList<>();

        for (CategoryEntity category:categoryEntityList){
            if (categoryEntity.getCatId()==category.getParentCid()){
                childrenList.add(category);
                getChildren(category,categoryEntityList);
            }
        }
        categoryEntity.setChildren(childrenList);

    }

}