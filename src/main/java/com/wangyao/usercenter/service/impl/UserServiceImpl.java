package com.wangyao.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangyao.usercenter.common.ErrorCode;
import com.wangyao.usercenter.constant.UserConstant;
import com.wangyao.usercenter.exception.BusinessException;
import com.wangyao.usercenter.mapper.UserMapper;
import com.wangyao.usercenter.model.pojo.User;
import com.wangyao.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务实现类
 * @author 王尧
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    public static final String SALT = "abc";


    @Autowired
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验空
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"参数为空");
        }
        //账户不小于4位
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"用户账号过短");
        }
        //账户不能为特殊字符(正则)
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*\n" +
                "（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"账号存在特殊字符");
        }
        //密码不小于8位
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"用户密码过短");
        }
        //两次密码一致
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"两次密码不一致");
        }
        //账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"账号重复");
        }
        //加盐加密
        User user = new User();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        int result = userMapper.insert(user);
        if(result < 1){
            throw new BusinessException(ErrorCode.INNER_ERROR,"注册内部异常");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验空
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"参数为空");
        }
        //账户不小于4位
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"用户账号过短");
        }
        //账户不能为特殊字符(正则)
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*\n" +
                "（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"账号存在特殊字符");
        }
        //密码不小于8位
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROE,"用户密码过短");
        }
        //账户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes()));
        User user = userMapper.selectOne(queryWrapper);
        //账户不存在
        if(user == null){
            throw new BusinessException(ErrorCode.LOGIN_ERROR,"登录账号不存在，请注册");
        }
        //脱敏
        User safetyUser = getSafetyUser(user);
        //登录态存到session中
        HttpSession session = request.getSession();
        session.setAttribute(UserConstant.USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User user){
        if(user == null){
            throw new BusinessException(ErrorCode.NULL_ERROE,"脱敏传入对象为空");
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setUsername(user.getUsername());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setCreateTime(new Date());
        safeUser.setUpdateTime(new Date());
        safeUser.setUserRole(user.getUserRole());
        return safeUser;
    }

    @Override
    public Integer logout(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INNER_ERROR,"退出内部异常");
        }
        return 1;
    }
}




