package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0422.bean.UserAddress;
import com.atguigu.gmall0422.bean.UserInfo;
import com.atguigu.gmall0422.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 订单控制层
 * @author: Mr.Lei
 * @create: 2019-10-03 20:48
 **/
@RestController
public class orderController {

    //@Autowired
    @Reference //com.alibaba 作为消费端
    private UserInfoService userInfoService;

    @RequestMapping("getAddressById")
    public List<UserAddress> getAddressById(String userid){

        return userInfoService.getAddressByUserId(userid);
    }
}

