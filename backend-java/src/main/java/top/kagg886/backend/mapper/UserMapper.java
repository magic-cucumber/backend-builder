package top.kagg886.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.kagg886.backend.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 