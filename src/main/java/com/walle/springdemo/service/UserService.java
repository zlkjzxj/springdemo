package com.walle.springdemo.service;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public List<User> getUsers() {
        return userDao.getGirls();
    }

    public User getUserById(int id) {
        return userDao.getGirlById(id);
    }

    @Transactional
    public int insert(User user) {
        User user1 = new User();
        user1.setId(1005);
        user1.setName("fuck2");
        userDao.insert(user1);

        User user2 = new User();
        user2.setId(1001);
        user2.setName("fuck1");
        return userDao.insert(user2);
    }
}
