package com.atguigu.gmall0422.service;


import com.atguigu.gmall0422.bean.UserAddress;
import com.atguigu.gmall0422.bean.UserInfo;

import java.util.List;

public interface UserInfoService {

    //查询所有数据
    List<UserInfo> getAll();

    //根据Loginname查询
    List<UserInfo> getUserByLoginName(UserInfo userInfo);

    //添加数据
    void addUser(UserInfo userInfo);

    //修改数据
    void updateUser(UserInfo userInfo);

    //删除数据
    void deleteUser(UserInfo userInfo);

    //根据用户id查询用户地址
    public List<UserAddress> getAddressByUserId(String userid);

}
