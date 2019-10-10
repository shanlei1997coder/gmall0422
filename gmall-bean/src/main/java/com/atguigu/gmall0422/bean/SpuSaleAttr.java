package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品销售属性名称
 * @author: Mr.Lei
 * @create: 2019-10-09 21:13
 **/
@Data
public class SpuSaleAttr implements Serializable {

    @Id
    @Column
    String id ;

    @Column
    String spuId;

    @Column
    String saleAttrId;

    @Column
    String saleAttrName;

    //封装商品基本属性值的集合
    @Transient
    private List<SpuSaleAttrValue> spuSaleAttrValueList;
}

