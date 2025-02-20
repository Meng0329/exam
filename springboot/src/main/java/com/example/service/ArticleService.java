package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Article;
import com.example.mapper.ArticleMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 帖子信息业务层方法
 */
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public void add(Article article) {
        Account currentUser = TokenUtils.getCurrentUser();
        article.setStudentId(currentUser.getId());
        article.setTime(DateUtil.now());
        articleMapper.insert(article);
    }

    public void updateById(Article article) {
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    public List<Article> selectRandom() {
        List<Article> list = articleMapper.selectAll(new Article());
        Collections.shuffle(list);
        if (list.size() > 3) {
            return list.subList(0, 3);
        }
        return list;
    }
}
