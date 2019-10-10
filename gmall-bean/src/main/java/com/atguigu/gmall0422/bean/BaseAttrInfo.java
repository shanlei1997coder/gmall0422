package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @program: gmall0422
 * @description: 平台属性
 * @author: Mr.Lei
 * @create: 2019-10-08 18:34
 **/
@Data
public class BaseAttrInfo implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //设置主键自增策略  mysql为IDENTITY  oracle为AUTO
    private String id;
    @Column
    private String attrName;
    @Column
    private String catalog3Id;


    //为了添加平台属性进行封装
    @Transient  //不进行序列化，使用数据库中不存在的字段进行传递，只是为了业务使用
    private List<BaseAttrValue> attrValueList;
}

