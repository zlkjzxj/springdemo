package com.walle.springdemo.dao;

import com.walle.springdemo.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from user")
    public List<User> getGirls();

    @Select("select * from user where id = #{id}")
    public User getGirlById(@PathVariable("id") int id);

    @Insert("insert into user (id,name) values (#{id},#{name})")
    public int insert(User user);
}

