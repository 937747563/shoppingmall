package com.zh.commodity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级分类VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    private String catalogId;       //1级父分类ID
    private List<Catelog3Vo> catalog3List;  //3级子分类
    private String id;          //
    private String name;        //


    /**
     * 三级分类VO
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id;      //2级父分类ID
        private String id;
        private String name;
    }

}


