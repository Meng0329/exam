package com.example.controller;

import com.example.common.Result;
import com.example.entity.ExamPlan;
import com.example.service.ExamPlanService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试安排前端请求接口
 */
@RestController
@RequestMapping("/examPlan")
public class ExamPlanController {

    @Resource
    private ExamPlanService examPlanService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody ExamPlan examPlan) {
        examPlanService.add(examPlan);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody ExamPlan examPlan) {
        examPlanService.updateById(examPlan);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        examPlanService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        examPlanService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        ExamPlan examPlan = examPlanService.selectById(id);
        return Result.success(examPlan);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ExamPlan examPlan) {
        List<ExamPlan> list = examPlanService.selectAll(examPlan);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(ExamPlan examPlan,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ExamPlan> pageInfo = examPlanService.selectPage(examPlan, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
