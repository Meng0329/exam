package com.example.mapper;

import com.example.entity.ExamPlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ExamPlanMapper {

    int insert(ExamPlan examPlan);

    void updateById(ExamPlan examPlan);

    void deleteById(Integer id);

    @Select("select * from `examPlan` where id = #{id}")
    ExamPlan selectById(Integer id);

    List<ExamPlan> selectAll(ExamPlan examPlan);

}
