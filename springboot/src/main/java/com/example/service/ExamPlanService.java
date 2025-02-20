package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.ExamPlan;
import com.example.mapper.ExamPlanMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试安排业务层方法
 */
@Service
public class ExamPlanService {

    @Resource
    private ExamPlanMapper examPlanMapper;

    public void add(ExamPlan examPlan) {
        examPlan.setTime(DateUtil.now());
        examPlanMapper.insert(examPlan);
    }

    public void updateById(ExamPlan examPlan) {
        examPlanMapper.updateById(examPlan);
    }

    public void deleteById(Integer id) {
        examPlanMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            examPlanMapper.deleteById(id);
        }
    }

    public ExamPlan selectById(Integer id) {
        return examPlanMapper.selectById(id);
    }

    public List<ExamPlan> selectAll(ExamPlan examPlan) {
        return examPlanMapper.selectAll(examPlan);
    }

    public PageInfo<ExamPlan> selectPage(ExamPlan examPlan, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExamPlan> list = examPlanMapper.selectAll(examPlan);
        return PageInfo.of(list);
    }

}
