package top.kagg886.backend.interceptor;

import top.kagg886.backend.entity.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireLogin {
    User.Role[] require() default {User.Role.USER, User.Role.ADMIN};
}
