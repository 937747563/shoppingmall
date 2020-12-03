package com.zh.commodity.service.impl;

import com.zh.commodity.entity.*;
import com.zh.commodity.fegin.CouponFeignService;
import com.zh.commodity.fegin.SearchFeignService;
import com.zh.commodity.fegin.StockFeignService;
import com.zh.commodity.service.*;
import com.zh.commodity.vo.SkuHasStockVo;
import com.zh.commodity.vo.SpuSaveVo;
import com.zh.commodity.vo.spuSaveVoUtil.*;
import com.zh.common.to.SkuReductionTo;
import com.zh.common.to.SpuBoundTo;
import com.zh.common.to.es.SkuEsModel;
import com.zh.common.utils.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zh.common.utils.PageUtils;
import com.zh.common.utils.Query;

import com.zh.commodity.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StockFeignService stockFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 新增商品(保存商品信息)
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {

        //1、保存spu基本信息   pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);

        //2、保存spu描述图片信息 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        //3、保存spu图集信息   pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImges(spuInfoEntity.getId(),images);

        //4、保存spu规格参数   pms_sku_sale_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(atr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();

            productAttrValueEntity.setAttrId(atr.getAttrId());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setAttrValue(atr.getAttrValues());
            productAttrValueEntity.setQuickShow(atr.getShowDesc());

            AttrEntity attrEntity = attrService.getById(atr.getAttrId());     //录入的信息没有属性名，查出属性名
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        //5、保存spu积分信息   sms->sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        couponFeignService.saveSpuBounds(spuBoundTo);

        //5、保存当前spu对应的sku信息

        List<Skus> skus = spuSaveVo.getSkus();
        if (skus!=null && skus.size()!=0) {

            skus.forEach(item -> {

                //获取默认图片
                String defaulImg="";
                for (Images image:item.getImages()){
                    if (image.getDefaultImg()==1){
                        defaulImg=image.getImgUrl();
                    }
                }

                //1)sku基本信息     pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaulImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                //2)sku图片信息     pms_sku_images
                //TODO 没有图片路径的无需保存
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    item.getImages().forEach(image->{
                        if (image.getImgUrl()!=null){
                            skuImagesEntity.setImgUrl(image.getImgUrl());
                            skuImagesEntity.setDefaultImg(image.getDefaultImg());
                        }
                    });
                    return skuImagesEntity;
                }).filter(entiy->{
                    return StringUtils.isEmpty(entiy.getImgUrl());
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);

                //3)sku销售属性信息  pms_sku_sale_attr_value
                List<Attr> attrList = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrList.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    BeanUtils.copyProperties(attr,skuSaleAttrValueEntity);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //4)sku优惠信息   （调用优惠模块）sms->sms_sku_ladder/sms_sku_full_reduction/sms_member_price/
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount()>0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    couponFeignService.saveSkuReduction(skuReductionTo);
                }
            });
        }
    }

    /**
     * 查询spu信息
     */
    @Override
    public PageUtils querByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 商品上架
     */
    @Transactional
    @Override
    public void up(Long spuId) {
        SpuInfoEntity spuInfoEntity = this.getById(spuId);

        //sku包含的所有属性值
        List<ProductAttrValueEntity> productAttrValueEntityList = productAttrValueService.queryBySpuId(spuId);
        List<Long> attrIds = productAttrValueEntityList.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        //过滤attrId查找需要检索的attrId
        List<Long> searchAttrIds= attrService.selectSearchAttrs(attrIds);

        //筛选属性值 挑选attrId 在searchAttrIds里的
        Set<Long> idSet=new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> searchAttrsList = productAttrValueEntityList.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());

        List<SkuInfoEntity> skus= skuInfoService.getSkusBySpuId(spuId);


        // 调用远程服务（库存系统）
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Integer> skuIdStockMap=null;
        try{
            R<List<SkuHasStockVo>> skusHasStock = stockFeignService.getSkusHasStock(skuIdList);
            if (skusHasStock.getData()!=null){
                skuIdStockMap = skusHasStock.getData().stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getStock));
            }
        }catch (Exception e){
            log.error("库存远程服务调用查询异常",e);
        }


        //组装需要的数据,封账信息
        Map<Long, Integer> finalSkuIdStockMap = skuIdStockMap;
        List<SkuEsModel> skuEsModelList = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();

            //封装sku信息
            BeanUtils.copyProperties(sku,skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            //封装品牌信息
            BrandEntity brand = brandService.getById(spuInfoEntity.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());

            //封装分类信息
            CategoryEntity category = categoryService.getById(spuId);
            skuEsModel.setCatalogName(category.getName());

            //设置检索属性
            skuEsModel.setAttrs(searchAttrsList);

            //设置库存信息
            if (finalSkuIdStockMap!=null){
                skuEsModel.setHasStock(finalSkuIdStockMap.get(sku.getSkuId())>0 ? true : false);
            }else {
                skuEsModel.setHasStock(false);
            }

            //TODO 热度盘评分
            skuEsModel.setHotScore(0L);


            return skuEsModel;
        }).collect(Collectors.toList());

        // 调用远程服务 发送给ES保存
        searchFeignService.commodityUp(skuEsModelList);

        //修改sup 状态——>(上架)
        spuInfoEntity.setPublishStatus(1);
        this.updateById(spuInfoEntity);


    }
}