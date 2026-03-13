package top.kagg886.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kagg886.backend.entity.User;
import top.kagg886.backend.mapper.UserMapper;
import top.kagg886.backend.service.UserService;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);

        if (user == null || !Objects.equals(user.getPassword(), password)) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置创建时间和默认角色
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        // 保存用户
        save(user);
        return user;
    }
}
