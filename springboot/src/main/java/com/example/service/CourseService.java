package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 课程信息业务层方法
 */
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    public void add(Course course) {
        Account currentUser = TokenUtils.getCurrentUser();
        course.setTeacherId(currentUser.getId());
        courseMapper.insert(course);
    }

    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            courseMapper.deleteById(id);
        }
    }

    public Course selectById(Integer id) {
        return courseMapper.selectById(id);
    }

    public List<Course> selectAll(Course course) {
        return courseMapper.selectAll(course);
    }

    public PageInfo<Course> selectPage(Course course, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            course.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Course> list = courseMapper.selectAll(course);
        return PageInfo.of(list);
    }

}
