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
    public List<User> getUsers();

    @Select("select * from user where id = #{id}")
    public User getUserById(@PathVariable("id") int id);

    @Select("select * from user where name = #{name}")
    public User getUserByName(@PathVariable("name") String name);

    @Insert("insert into user (id,name,salt,password) values (#{id},#{name},#{salt},#{password})")
    public int insert(User user);
}

