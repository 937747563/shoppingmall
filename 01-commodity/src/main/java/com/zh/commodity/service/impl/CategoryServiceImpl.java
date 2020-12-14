package com.zh.commodity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zh.commodity.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.commodity.dao.CategoryDao;
import com.zh.commodity.entity.CategoryEntity;
import com.zh.commodity.service.CategoryService;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private StringRedisTemplate redisTemplate;

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

    /**
     * 前台：查找一级分类
     */
    @Override
    public List<CategoryEntity> getLevel1Categorys() {

        List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("cat_level", 1));

        return categoryEntityList;
    }


    /**
     * 查出所有分类：
     *      先从redis中查询，没有再调用getCatalogJsonFromDb方法，从Mysql中查询
     * 给redis中方json字符串，拿出json在转为对象（序列化与反序列化）
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        /**
         * TODO 1、设置空值缓存
         *      2、指定过期时间（加随机值）
         *      3、加锁
         */

        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)){
            //redis 中无所要查询数据  从mysql中查询——>放入redis
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDb();
            redisTemplate.opsForValue().set("catalogJSON",JSON.toJSONString(catalogJsonFromDb));
            return catalogJsonFromDb;
        }
        //不为空
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>(){});
        return result;
    }


    /**
     * 查出所有分类 从Mysql中查询
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb(){
        //所有一级分类
        List<CategoryEntity> level1Categorys = getLevel1Categorys();

        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {

            //查询每个一级分类的所有二级分类
            List<CategoryEntity> level2categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));

            List<Catelog2Vo> catelog2VoList = null;
            if (level2categoryEntities != null) {
                //封装二级分类
                catelog2VoList = level2categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());

                    //查找当前二级分类的所有三级分类
                    List<CategoryEntity> level3CategoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", l2.getCatId()));
                    if (level3CategoryEntityList!=null){
                        //封装三级分类
                        List<Catelog2Vo.Catelog3Vo> catelog3VoList = level3CategoryEntityList.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(),l3.getCatId().toString(),l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3VoList);
                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2VoList;
        }));

        return parent_cid;
    }

}