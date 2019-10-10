package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: gmall0422
 * @description: 商品一级分类
 * @author: Mr.Lei
 * @create: 2019-10-08 18:31
 **/
@Data
public class BaseCatalog1 implements Serializable {

    @Id
    @Column
    private String id;
    @Column
    private String name;
}

