package top.kagg886.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kagg886.backend.entity.User;
import top.kagg886.backend.interceptor.RequireLogin;
import top.kagg886.backend.interceptor.RequireLoginInterceptor;
import top.kagg886.backend.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    @RequireLogin
    public User getProfile(@RequestAttribute(RequireLoginInterceptor.SESSION_NAME) User user) {
        return user;
    }

    @PutMapping("/profile")
    @RequireLogin
    public User updateProfile(@RequestAttribute(RequireLoginInterceptor.SESSION_NAME) User user, @RequestBody User userInfo) {
        user.setEmail(userInfo.getEmail());
        user.setPhone(userInfo.getPhone());
        user.setPassword(userInfo.getPassword());
        userService.updateById(user);
        return user;
    }

    @PostMapping
    @RequireLogin(require = {User.Role.ADMIN})
    public User createUser(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/{userId}")
    @RequireLogin(require = {User.Role.ADMIN})
    public boolean updateUser(@PathVariable Integer userId, @RequestBody User user) {
        user.setUserId(userId);
        return userService.updateById(user);
    }

    @DeleteMapping("/{userId}")
    @RequireLogin(require = {User.Role.ADMIN})
    public boolean deleteUser(@PathVariable Integer userId) {
        return userService.removeById(userId);
    }

    @GetMapping("/{userId}")
    @RequireLogin(require = {User.Role.ADMIN})
    public User getUser(@PathVariable Integer userId) {
        return userService.getById(userId);
    }
}
