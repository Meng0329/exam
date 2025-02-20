package com.example.mapper;

import com.example.entity.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {

    int insert(Article article);

    void updateById(Article article);

    void deleteById(Integer id);

    @Select("select article.*, student.name as studentName from `article` " +
            "left join student on article.student_id = student.id " +
            "where article.id = #{id}")
    Article selectById(Integer id);

    List<Article> selectAll(Article article);

}
