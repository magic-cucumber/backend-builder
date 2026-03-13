package top.kagg886.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.kagg886.backend.entity.User;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息
     */
    User login(String username, String password);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功的用户
     */
    User register(User user);
} 