package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: gmall0422
 * @description: 商品二级分类
 * @author: Mr.Lei
 * @create: 2019-10-08 18:33
 **/
@Data
public class BaseCatalog2 implements Serializable {

    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column
    private String catalog1Id;
}

