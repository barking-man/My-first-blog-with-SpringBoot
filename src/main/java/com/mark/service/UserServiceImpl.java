package com.mark.service;/*
 * @project: myblog
 * @name: UserServiceImpl
 * @author: Mark
 * @Date: 2021/4/12
 * @Time: 23:12
 */

import com.mark.dao.UserRepository;
import com.mark.pojo.User;
import com.mark.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
        } else {
            user.setUpdateTime(new Date());
        }
        userRepository.save(user);
        return null;
    }

    @Override
    public User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
