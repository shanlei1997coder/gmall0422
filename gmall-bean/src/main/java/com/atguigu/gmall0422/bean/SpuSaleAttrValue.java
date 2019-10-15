package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @program: gmall0422
 * @description: 商品销售属性值
 * @author: Mr.Lei
 * @create: 2019-10-09 21:14
 **/
@Data
public class SpuSaleAttrValue implements Serializable {

    @Id
    @Column
    String id ;

    @Column
    String spuId;

    @Column
    String saleAttrId;

    @Column
    String saleAttrValueName;

    // 用于表示该商品选中的销售属性  如果为0, 表示未选中  1表示选中
    @Transient
    String isChecked;

}

