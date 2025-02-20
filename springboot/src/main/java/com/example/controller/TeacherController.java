package com.example.controller;

import com.example.common.Result;
import com.example.entity.Teacher;
import com.example.service.TeacherService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师信息前端请求接口
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Teacher teacher) {
        teacherService.add(teacher);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        teacherService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        teacherService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Teacher teacher = teacherService.selectById(id);
        return Result.success(teacher);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Teacher teacher) {
        List<Teacher> list = teacherService.selectAll(teacher);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Teacher teacher,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Teacher> pageInfo = teacherService.selectPage(teacher, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
