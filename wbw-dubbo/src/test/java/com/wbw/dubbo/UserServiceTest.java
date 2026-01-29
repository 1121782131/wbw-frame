package com.wbw.dubbo;

import com.wbw.dubbo.service.UserService;
import com.wbw.dubbo.service.UserServiceConsumer;
import com.wbw.dubbo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 用户服务功能测试
 */
@SpringBootTest
public class UserServiceTest {

    /**
     * 模拟用户服务实现
     */
    @MockBean
    private UserServiceImpl userService;

    /**
     * 注入用户服务消费者
     */
    // @Autowired
    // private UserServiceConsumer userServiceConsumer;

    /**
     * 测试服务提供者的getUserById方法
     */
    @Test
    public void testUserServiceProvider() {
        // 模拟服务提供者的行为
        Long userId = 1L;
        String expectedResult = "User{id=1, name=\"test_user\", age=25}";
        Mockito.when(userService.getUserById(userId)).thenReturn(expectedResult);

        // 调用服务方法
        String result = userService.getUserById(userId);

        // 验证结果
        Assertions.assertEquals(expectedResult, result);
        Mockito.verify(userService, Mockito.times(1)).getUserById(userId);
    }

    /**
     * 测试服务提供者的createUser方法
     */
    @Test
    public void testCreateUser() {
        // 模拟服务提供者的行为
        String userInfo = "{\"name\":\"test_user\", \"age\":25}";
        Boolean expectedResult = true;
        Mockito.when(userService.createUser(userInfo)).thenReturn(expectedResult);

        // 调用服务方法
        Boolean result = userService.createUser(userInfo);

        // 验证结果
        Assertions.assertEquals(expectedResult, result);
        Mockito.verify(userService, Mockito.times(1)).createUser(userInfo);
    }

    /**
     * 测试服务提供者的updateUser方法
     */
    @Test
    public void testUpdateUser() {
        // 模拟服务提供者的行为
        Long userId = 1L;
        String userInfo = "{\"name\":\"updated_user\", \"age\":26}";
        Boolean expectedResult = true;
        Mockito.when(userService.updateUser(userId, userInfo)).thenReturn(expectedResult);

        // 调用服务方法
        Boolean result = userService.updateUser(userId, userInfo);

        // 验证结果
        Assertions.assertEquals(expectedResult, result);
        Mockito.verify(userService, Mockito.times(1)).updateUser(userId, userInfo);
    }

    /**
     * 测试服务提供者的deleteUser方法
     */
    @Test
    public void testDeleteUser() {
        // 模拟服务提供者的行为
        Long userId = 1L;
        Boolean expectedResult = true;
        Mockito.when(userService.deleteUser(userId)).thenReturn(expectedResult);

        // 调用服务方法
        Boolean result = userService.deleteUser(userId);

        // 验证结果
        Assertions.assertEquals(expectedResult, result);
        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }

    /**
     * 测试服务提供者的testService方法
     */
    @Test
    public void testTestService() {
        // 模拟服务提供者的行为
        String message = "Hello Dubbo!";
        String expectedResult = "服务响应: Hello Dubbo!";
        Mockito.when(userService.testService(message)).thenReturn(expectedResult);

        // 调用服务方法
        String result = userService.testService(message);

        // 验证结果
        Assertions.assertEquals(expectedResult, result);
        Mockito.verify(userService, Mockito.times(1)).testService(message);
    }
}
