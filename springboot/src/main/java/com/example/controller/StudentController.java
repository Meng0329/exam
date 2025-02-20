package com.example.controller;

import com.example.common.Result;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生信息前端请求接口
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        studentService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        studentService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Student student = studentService.selectById(id);
        return Result.success(student);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Student student) {
        List<Student> list = studentService.selectAll(student);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Student student,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Student> pageInfo = studentService.selectPage(student, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        return Result.success(studentService.count());
    }

}
