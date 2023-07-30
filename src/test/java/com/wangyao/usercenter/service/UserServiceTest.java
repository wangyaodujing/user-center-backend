package com.wangyao.usercenter.service;
import com.wangyao.usercenter.model.pojo.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 王尧
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUserService(){
        User user = new User();
        user.setUserAccount("wangyao");
        user.setUserPassword("wangyao123");
        user.setUsername("王尧");
        user.setAvatarUrl("http://xyd.creditjx.gov.cn/xyd_jx/dist/style/img/logo.png");
        user.setGender(0);
        user.setPhone("13199512623");
        user.setEmail("130605593@qq.com");
        boolean result = userService.save(user);
        Assertions.assertTrue(result);
    }

    @Test
    public void userRegister() {
        String userAccount = "";
        String userPassword = "123456";
        String checkPassword = "123456";
        long result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "abc";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "abcd";
        userPassword = "123456";
        checkPassword = "123456";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "abcd~";
        userPassword = "12345678";
        checkPassword = "12345678";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "abcd";
        userPassword = "12345678";
        checkPassword = "123456789";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "wangyao";
        userPassword = "12345678";
        checkPassword = "12345678";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
        userAccount = "dujing";
        userPassword = "12345678";
        checkPassword = "12345678";
        result1 = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1,result1);
    }
}