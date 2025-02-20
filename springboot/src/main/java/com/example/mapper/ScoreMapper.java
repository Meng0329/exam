package com.example.mapper;

import com.example.entity.Score;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ScoreMapper {

    int insert(Score score);

    void updateById(Score score);

    void deleteById(Integer id);

    @Select("select * from `score` where id = #{id}")
    Score selectById(Integer id);

    List<Score> selectAll(Score score);

}
