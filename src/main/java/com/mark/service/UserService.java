package com.mark.service;/*
 * @project: myblog
 * @name: UserService
 * @author: Mark
 * @Date: 2021/4/12
 * @Time: 23:11
 */

import com.mark.pojo.User;

public interface UserService {
    User checkUser(String username, String password);

    // 保存用户
    User saveUser(User user);

    // 根据昵称查询用户
    User getUserByNickname(String nickname);

    // 根据用户名查询用户
    User getUserByUsername(String username);
}
