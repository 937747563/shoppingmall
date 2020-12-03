package com.zh.stock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("wms_purchase_purchase_detail_relational")
public class PurchasePurchaseDetailRelationalEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 采购单id
     */
    private Long purchaseId;

    /**
     * 采购需求id
     */
    private Long purchaseDetailId;

}
