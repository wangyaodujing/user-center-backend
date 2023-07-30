package com.wangyao.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wangyao.usercenter.model.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 86157
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-07-19 17:21:25
 */
public interface UserService extends IService<User> {



    /**
     * 注册
     * @param userAccount 账号
     * @param userPassword 密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     *
     * @param userAccount 账号
     * @param userPassword 密码
     * @return
     */
    User userLogin(String userAccount, String userPassword,HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User getSafetyUser(User user);

    Integer logout(HttpServletRequest request);
}
