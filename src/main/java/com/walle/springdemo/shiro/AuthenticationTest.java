package com.walle.springdemo.shiro;

import com.walle.springdemo.bean.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

public class AuthenticationTest {
    /**
     * Authentication 验证
     * Authorization  授权
     * SessionManager
     * Cryptography 加密
     * <p>
     * 概念层
     * Subject 用户 第三方
     * <p>
     * SecurityManager  执行者 安全管理器 核心 负责与其他层进行交互
     * <p>
     * Realm  安全数据源 datasource
     */


    /**
     * spring security
     * <p>
     * 两个概念 Authentication & Authorization
     * 1.AuthenticationSecurity  设置用户密码和访问权限
     * 2.websecurity 允许哪些文件静态资源能够访问，那些不能访问
     * 2.HttpSecurity 哪些url能够访问,login,loginout csrf
     *
     * 缺点：
     */


    SimpleAccountRealm realm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        //授权的时候可以添加权限
        realm.addAccount("Jack", "1234", "admin", "user");
    }

    @Test
    public void shiroTest() {
        //1.构建securityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(realm);
        //创建主题
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Jack", "1234");
        subject.login(token);

        System.out.println("isAuthenticated:" + subject.isAuthenticated());

//        subject.logout();
//        System.out.println("isAuthenticated:" + subject.isAuthenticated());
        //授权测试
        subject.checkRoles("admin", "user");

    }

    public void testRoles(String... roles) {
        List<String> lists = CollectionUtils.asList(roles);
        for (int i = 0; i < lists.size(); i++) {
            System.out.println(lists.get(i));
        }
    }


    /*public static <T> List<T> asList(T... elements) {
        return elements != null && elements.length != 0 ? Arrays.asList(elements) : Collections.emptyList();
    }*/

    public static <T> List<T> asList(T... elements) {
        return elements != null && elements.length != 0 ? Arrays.asList(elements) : Collections.emptyList();
    }

    public static <T> List<T> addObject(T... elements) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < elements.length; i++) {
            list.add(elements[i]);
        }
        return list;

    }

    public static void main(String[] args) {
//        new AuthenticationTest().testRoles("david", "jack");
//        List<String> lists = asList();
//        System.out.println(lists);
        List<String> list = addObject("xx", "cc");
        for (String xx : list) {
            System.out.println(xx);
        }
        /*List<User> list = addObject(new User("Jack", "xxx"), new User("David", "cc"));
        for (User xx : list) {
            System.out.println(xx.getName());
        }*/
    }
}
