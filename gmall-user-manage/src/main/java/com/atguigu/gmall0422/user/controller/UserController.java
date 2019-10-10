package com.atguigu.gmall0422.user.controller;

import com.atguigu.gmall0422.bean.UserInfo;
import com.atguigu.gmall0422.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 控制层
 * @author: Mr.Lei
 * @create: 2019-10-03 20:22
 **/
@RestController
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("findAll")
    public List<UserInfo> findAll(){
        return userInfoService.getAll();
    }

}

