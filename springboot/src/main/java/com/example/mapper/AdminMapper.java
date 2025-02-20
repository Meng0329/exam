package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {

    int insert(Admin admin);

    void updateById(Admin admin);

    void deleteById(Integer id);

    @Select("select * from `admin` where id = #{id}")
    Admin selectById(Integer id);

    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);

    List<Admin> selectAll(Admin admin);

}
