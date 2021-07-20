package com.mark.web;/*
 * @project: myblog
 * @name: RegistrationController
 * @author: Mark
 * @Date: 2021/7/14
 * @Time: 20:24
 */

import com.mark.pojo.User;
import com.mark.service.UserService;
import com.mark.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    String baseimg = "";


//    @PostMapping("/regis")
//    public String registration(User user,
//                               @RequestParam("imgFile")MultipartFile imgFile) throws IOException {
//        String baseimg = "";
//        // 保存头像
//        if (imgFile != null) {
//            BASE64Encoder base64Encoder = new BASE64Encoder();
//            // 保存图片字符流到数据库
//            baseimg = "data:" + imgFile.getContentType() + ";base64," + base64Encoder.encode(imgFile.getBytes());
//        }
//        user.setAvatar(baseimg);
//        String pwd = user.getPassword();
//        user.setPassword(MD5Utils.code(pwd));
//        userService.saveUser(user);
//        return "login";
//    }

    /**
     * 处理ajax上传过来图片
     * 因为通过ajax提交图片时，图片数据格式需要特殊设置，注册时为了不与用户其他信息冲突，故提前保存到全局变量
     */
    @PostMapping("/upLoadImg")
    @ResponseBody
    public void upLoadImg(MultipartFile imgFile) throws IOException {
        // 保存头像
        if (imgFile != null) {
            System.out.println("==================>");
            System.out.println("came to upLoadImg!");
            BASE64Encoder base64Encoder = new BASE64Encoder();
            // 保存图片字符流到数据库
            baseimg = "data:" + imgFile.getContentType() + ";base64," + base64Encoder.encode(imgFile.getBytes());
        } else {
            System.out.println("imgFile is null !");
        }
    }

    @PostMapping("/registration")
    @ResponseBody
    public String registration(User user) throws IOException {
        String msg = "ok";
        // 用户名正则：4到16位（字母，数字，下划线，减号）
        String usernameReg = "^[a-zA-Z0-9_]{3,16}$";
        // 密码正则：6~18位字母和数字混合组成
        String pwdReg = "^[a-zA-Z0-9]{6,18}$";

        System.out.println("==================>");
        System.out.println("came to username check");
        // 验证用户名或密码是否输入正确
        if (!user.getUsername().matches(usernameReg) && !user.getPassword().matches(pwdReg)) {
            msg = "errorInput";
            return msg;
        }
        if (!user.getUsername().matches(usernameReg)) {
            msg = "usernameError";
            return msg;
        }
        if (!user.getPassword().matches(pwdReg)) {
            msg = "passwordError";
            return msg;
        }

        // 验证用户名及昵称是否存在
        User ret_user1 = userService.getUserByNickname(user.getNickname());
        User ret_user2 = userService.getUserByUsername(user.getUsername());
        if (ret_user1 != null && ret_user2 != null) {
            msg = "dupNicknameUsername";
            return msg;
        }
        if (ret_user1 != null) {
            msg = "dupNickname";
            return msg;
        }
        if (ret_user2 != null) {
            msg = "dupUsername";
            return msg;
        }

        String pwd = user.getPassword();
        user.setPassword(MD5Utils.code(pwd));
        user.setAvatar(baseimg);
        userService.saveUser(user);
        System.out.println("registration is end !");

        return msg;
    }

}
