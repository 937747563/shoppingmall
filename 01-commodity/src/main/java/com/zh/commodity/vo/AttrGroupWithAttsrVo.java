package com.zh.commodity.vo;

import com.zh.commodity.entity.AttrEntity;
import com.zh.commodity.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttsrVo extends AttrGroupEntity {

    private List<AttrEntity> attrs;
}
