package com.wangyao.usercenter.model.request;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户注册请求体
 * @author 王尧
 * @version 1.0
 */
@Data
@ToString
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -5218593581620776245L;

    String userAccount;

    String userPassword;

}
