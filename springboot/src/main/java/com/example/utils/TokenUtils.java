package com.example.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.common.Constants;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * Token工具类
 */
@Component
public class TokenUtils {
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);

    @Resource
    private AdminService adminService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;

    private static AdminService staticAdminService;
    private static TeacherService staticTeacherService;
    private static StudentService staticStudentService;

    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticTeacherService = teacherService;
        staticStudentService = studentService;
    }

    /**
     * 生成JWT token
     */
    public static String createToken(String data, String sign) {
        // audience是存储数据的一个媒介  存储用户ID和用户的角色  1-ADMIN
        return JWT.create().withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 设置过期时间1天后
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户
     */
    public static Account getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader(Constants.TOKEN);
            String audience = JWT.decode(token).getAudience().get(0);
            String[] userRole = audience.split("-");
            Integer userId = Integer.valueOf(userRole[0]);
            String role = userRole[1];
            if (RoleEnum.ADMIN.name().equals(role)) {
                return staticAdminService.selectById(userId);
            }
            if (RoleEnum.TEACHER.name().equals(role)) {
                return staticTeacherService.selectById(userId);
            }
            if (RoleEnum.STUDENT.name().equals(role)) {
                return staticStudentService.selectById(userId);
            }
        } catch (Exception e) {
            log.error("获取当前登录用户出错", e);
        }
        return null;
    }

}
