package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: gmall0422
 * @description: 用户地址
 * @author: Mr.Lei
 * @create: 2019-10-03 20:49
 **/
@Data
public class UserAddress implements Serializable{ //实现序列化接口 用于网络之间通信

    @Column
    @Id
    private String id;
    @Column
    private String userAddress;
    @Column
    private String userId;
    @Column
    private String consignee; //收件人
    @Column
    private String phoneNum;
    @Column
    private String isDefault;
}

