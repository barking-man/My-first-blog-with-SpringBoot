package com.mark.web;/*
 * @project: myblog
 * @name: LoginController
 * @author: Mark
 * @Date: 2021/7/14
 * @Time: 14:52
 */

import com.mark.pojo.User;
import com.mark.service.BlogService;
import com.mark.service.TagService;
import com.mark.service.TypeService;
import com.mark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /************登录管理*************/
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/logIn")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model,
                        @PageableDefault(size = 5, sort = ("updateTime"), direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            model.addAttribute("page", blogService.listBlog(pageable));
            model.addAttribute("types", typeService.listTypeTop(6));
            model.addAttribute("tags", tagService.listTagTop(10));
            model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
            return "index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    /************分割线*************/

    @GetMapping("/index")
    public String index(@PageableDefault(size = 5, sort = ("updateTime"), direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = ("updateTime"), direction = Sort.Direction.DESC) Pageable pageable,
                         String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        // 返回前三个最新的博客
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }
}
