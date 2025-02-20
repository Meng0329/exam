package com.example.controller;

import com.example.common.Result;
import com.example.entity.TestPaper;
import com.example.service.TestPaperService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 试卷信息前端请求接口
 */
@RestController
@RequestMapping("/testPaper")
public class TestPaperController {

    @Resource
    private TestPaperService testPaperService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody TestPaper testPaper) throws ParseException {
        testPaperService.add(testPaper);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody TestPaper testPaper) {
        testPaperService.updateById(testPaper);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        testPaperService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        testPaperService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        TestPaper testPaper = testPaperService.selectById(id);
        return Result.success(testPaper);
    }

    @GetMapping("/check/{id}")
    public Result check(@PathVariable Integer id) {
        testPaperService.checkTestPaper(id);
        return Result.success();
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(TestPaper testPaper) {
        List<TestPaper> list = testPaperService.selectAll(testPaper);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(TestPaper testPaper,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) throws ParseException {
        PageInfo<TestPaper> pageInfo = testPaperService.selectPage(testPaper, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectRandom")
    public Result selectRandom() throws ParseException {
        List<TestPaper> list = testPaperService.selectRandom();
        return Result.success(list);
    }

    @GetMapping("/count")
    public Result count() {
        return Result.success(testPaperService.count());
    }

}
