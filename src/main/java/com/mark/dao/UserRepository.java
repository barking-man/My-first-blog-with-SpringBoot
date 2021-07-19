package com.mark.dao;/*
 * @project: myblog
 * @name: UserRepository
 * @author: Mark
 * @Date: 2021/4/12
 * @Time: 23:13
 */

import com.mark.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

// 继承JpaRepository后可直接使用其操作数据库的方法
public interface UserRepository extends JpaRepository<User,Long> {

    // 根据用户名和密码查询
    User findByUsernameAndPassword(String username, String password);

    // 根据昵称查询
    User findByNickname(String nickname);
}
