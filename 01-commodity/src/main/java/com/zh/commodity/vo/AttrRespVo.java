package com.zh.commodity.vo;

import com.zh.commodity.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrRespVo extends AttrEntity {



    /**
     *  所属分类名字
     */
    private String catelogName;

    /**
     * 所属分组名字
     */
    private String groupName;
    private String attrGroupId;
    /**
     * 路径信息
     */
    private Long[] catelogPath;
}
