package com.zh.commodity.vo;


import com.zh.commodity.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrVo extends AttrEntity {

    /**
     * 分组Id
     */
    private Long attrGroupId;
}
