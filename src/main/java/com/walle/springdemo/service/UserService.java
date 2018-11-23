package com.walle.springdemo.service;

import com.walle.springdemo.bean.User;
import com.walle.springdemo.dao.UserDao;
import com.walle.springdemo.exception.GlobalException;
import com.walle.springdemo.redis.RedisService;
import com.walle.springdemo.redis.UserKey;
import com.walle.springdemo.result.CodeMsg;
import com.walle.springdemo.utils.MD5Util;
import com.walle.springdemo.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisService redisService;

    public final static String COOKIE_NAME_TOKEN = "token";

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    //这里加一个对象级的缓存 ,但是这里存在一个问题就是如果update用户的话，必须把用户缓存清理掉，把token重新set
    public User getUserById(int id) {
        //先判断缓存里面有没有用户
        User user = redisService.get(UserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        user = userDao.getUserById(id);
        if (user != null) {
            redisService.set(UserKey.getById, "" + id, user);
        }
        return user;
    }

//    public User getUserByName(String name) {
//        return userDao.getUserByName(name);
//    }

    public int insertUser(User user) {
        return userDao.insert(user);
    }

    /**
     * 1.先根据用户名去查用户
     * 2.取出插过来用户的密码和盐，
     * 把参数密码经过盐加密之后比对数据库密码和加密后的密码是否相同
     * 3.随机生成token，保存到数据库并添加到cookie中
     *
     * @param response
     * @param name
     * @param password
     * @return
     */
    public boolean login(HttpServletResponse response, String name, String password) {
        User user = userDao.getUserByName(name);
        //如果查到用户 就用加密的密码和数据库的salt再次加密比对密码是否一样，如果一样，登录成功
        if (user == null) {
            throw new GlobalException(CodeMsg.USERNOTEXIST_ERROR);
        }
        String pass = MD5Util.formPassToDbPass(password, user.getSalt());
        if (!pass.equals(user.getPassword())) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        Cookie cookie = addCookie(token, user);
        response.addCookie(cookie);
        return true;
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

    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if (user != null) {
            Cookie cookie = addCookie(token, user);
            response.addCookie(cookie);
        }
        return user;
    }

    public Cookie addCookie(String token, User user) {
        //token没不要每次都生成，要不然每个功能都调用，不停的生成新的
        //只需要登录的时候生成就行了
//        String token = UUIDUtil.uuid();
        boolean b = redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        return cookie;
    }
}
