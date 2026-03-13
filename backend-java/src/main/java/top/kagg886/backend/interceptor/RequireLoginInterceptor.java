package top.kagg886.backend.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yaml.snakeyaml.util.ArrayUtils;
import top.kagg886.backend.dto.BaseResponse;
import top.kagg886.backend.entity.User;
import top.kagg886.backend.service.UserService;
import top.kagg886.backend.util.JWT;

@Slf4j
@Component
public class RequireLoginInterceptor implements HandlerInterceptor {
    public static final String SESSION_NAME = "USER_SESSION";
    private final UserService userService;

    public RequireLoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod method) {
            //有RequireLogin注解，没有Authorization --> 跳转到登录
            //有RequireLogin注解，有Authorization --> 验证token
            //无RequireLogin注解，有Authorization --> 验证token
            //无RequireLogin注解，无Authorization --> 放行

            RequireLogin permission = method.getMethodAnnotation(RequireLogin.class);
            val token = request.getHeader("Authorization");

            if (permission == null && StringUtils.isBlank(token)) { // 无注解，无token
                return true;
            }

            if (permission != null && StringUtils.isBlank(token)) { // 有注解，无token
                throw new BaseResponse.Wrapped(-2, "请求头错误");
            }

            // 有token就验证token
            User login;
            try {
                login = JWT.verify(token);
            } catch (Exception e) {
                log.warn("有token验证失败, token: {}", token, e);
                throw new BaseResponse.Wrapped(-2, "token错误");
            }
            request.setAttribute(SESSION_NAME, userService.getById(login.getUserId()));

            // 无注解，有token
            if (permission == null) {
                return true;
            }

            // 有注解，有token
            User.Role[] roles = permission.require();
            if (!ArrayUtils.toUnmodifiableList(roles).contains(login.getRole())) {
                throw new BaseResponse.Wrapped(-1, "权限不足");
            }
            return true;
        }
        return true;
    }
}
