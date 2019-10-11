package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: gmall0422
 * @description: sku平台属性
 * @author: Mr.Lei
 * @create: 2019-10-10 21:16
 **/
@Data
public class SkuAttrValue implements Serializable{

    @Id
    @Column
    String id;

    @Column
    String attrId;

    @Column
    String valueId;

    @Column
    String skuId;
}

