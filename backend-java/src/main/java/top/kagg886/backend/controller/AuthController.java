package top.kagg886.backend.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.kagg886.backend.dto.BaseResponse;
import top.kagg886.backend.entity.User;
import top.kagg886.backend.service.UserService;
import top.kagg886.backend.util.JWT;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Data
    public static class LoginInfo {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterInfo {
        private String username;
        private String email;
        private String phone;
        private String password;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginInfo loginInfo) {
        if (loginInfo.username == null || loginInfo.password == null) {
            throw new BaseResponse.Wrapped("用户名和密码不能为空");
        }

        User user = userService.login(loginInfo.username, loginInfo.password);
        user.setPassword(null);

        return JWT.publish(user);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public User register(@RequestBody RegisterInfo user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new BaseResponse.Wrapped("用户名和密码不能为空");
        }

        User registeredUser = userService.register(
                User.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .password(user.getPassword())
                        .role(User.Role.USER)
                        .build()
        );
        // 返回用户信息，但不包含密码
        registeredUser.setPassword(null);
        return registeredUser;
    }
}
