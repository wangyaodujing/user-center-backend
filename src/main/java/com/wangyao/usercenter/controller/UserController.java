package com.wangyao.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangyao.usercenter.common.BaseResponse;
import com.wangyao.usercenter.common.ErrorCode;
import com.wangyao.usercenter.common.ResultUtil;
import com.wangyao.usercenter.constant.UserConstant;
import com.wangyao.usercenter.exception.BusinessException;
import com.wangyao.usercenter.model.pojo.User;
import com.wangyao.usercenter.model.request.UserLoginRequest;
import com.wangyao.usercenter.model.request.UserRegisterRequest;
import com.wangyao.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 王尧
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtil.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        Integer result = userService.logout(request);
        return ResultUtil.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserById(@RequestBody long id, HttpServletRequest request) {
        if (isAdmin(request)) throw new BusinessException(ErrorCode.NO_AUTH);
        if (id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROE);
        }
        boolean result = userService.removeById(id);
        return ResultUtil.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentState(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //根据用户id查询数据库中实时的用户数据
        User u = userService.getById(user.getId());
        return ResultUtil.success(userService.getSafetyUser(u));
    }

    private boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return user == null || user.getUserRole() != UserConstant.ADMIN_ROLE;
    }
}
