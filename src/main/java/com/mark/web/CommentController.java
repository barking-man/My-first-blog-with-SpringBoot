package com.mark.web;/*
 * @project: myblog
 * @name: CommentController
 * @author: Mark
 * @Date: 2021/7/6
 * @Time: 14:41
 */

import com.mark.pojo.Comment;
import com.mark.pojo.User;
import com.mark.service.BlogService;
import com.mark.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    // 从application.yml文件中取出自定义的参数
    @Value("${comment.avatar}")
    private String avatar;

    // 显示博客评论
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments" ,commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession httpSession) {
        System.out.println("==============>");
        System.out.println("came to comments");
        User user = (User) httpSession.getAttribute("user");
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        // 目前只有管理员可登录，因此user不为空，即当前session中存储的是管理员
        if (user.getType() == 1) {
            // 管理员
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            // 普通访客
            comment.setAvatar(user.getAvatar());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
