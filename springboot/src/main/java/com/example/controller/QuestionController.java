package com.example.controller;

import com.example.common.Result;
import com.example.entity.Question;
import com.example.service.QuestionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目信息前端请求接口
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Question question) {
        questionService.add(question);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Question question) {
        questionService.updateById(question);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        questionService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        questionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Question question = questionService.selectById(id);
        return Result.success(question);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Question question) {
        List<Question> list = questionService.selectAll(question);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Question question,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Question> pageInfo = questionService.selectPage(question, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/count")
    public Result count() {
        return Result.success(questionService.count());
    }

}
