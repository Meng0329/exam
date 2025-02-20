package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从http请求标头里面拿到token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isNull(token)) {
            // 如果没拿到，那么再从请求参数里拿一次
            request.getParameter(Constants.TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isNull(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        try {
            String audience = JWT.decode(token).getAudience().get(0);
            String userId = audience.split("-")[0];
            String role = audience.split("-")[1];
            // 根据用户角色判断用户属于哪个数据库表 然后查询用户数据
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            }
            if (RoleEnum.TEACHER.name().equals(role)) {
                account = teacherService.selectById(Integer.valueOf(userId));
            }
            if (RoleEnum.STUDENT.name().equals(role)) {
                account = studentService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 根据token里面携带的用户ID去对应的角色表查询  没查到 所有报了这个“用户不存在”错误
        if (ObjectUtil.isNull(account)) {
            // 用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        try {
            // 通过用户的密码作为密钥再次验证token的合法性
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token);  // 验证token
        } catch (JWTVerificationException e) {
            // 用户不存在
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        return true;
    }

}
