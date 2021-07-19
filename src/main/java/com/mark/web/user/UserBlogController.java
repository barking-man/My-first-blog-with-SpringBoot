package com.mark.web.user;/*
 * @project: myblog
 * @name: UserBlogControoler
 * @author: Mark
 * @Date: 2021/7/16
 * @Time: 21:41
 */

import com.mark.pojo.Blog;
import com.mark.pojo.User;
import com.mark.service.BlogService;
import com.mark.service.TagService;
import com.mark.service.TypeService;
import com.mark.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserBlogController {

    private static final String INPUT = "user/blogs-input";
    private static final String REDIRECT_LIST = "redirect:/user/userBlog";

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    // 列出当前用户的所有博客
    @GetMapping("/userBlog")
    public String userBlog(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                           BlogQuery blogQuery, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        blogQuery.setUserId(user.getId());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        return "user/userBlog";
    }

    // 进入新增博客编辑界面
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags" , tagService.listTag());
        return INPUT;
    }

    // 进入已有博客修改界面
    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable("id") Long id, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags" , tagService.listTag());

        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    // 将新增或修改的博客发布
    @PostMapping("/blogs")
    public String publish(Blog blog, RedirectAttributes redirectAttributes, HttpSession session,
                       @RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        // 存储图片字符流
        String baseimg = "";
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        // 图片上传
        if (imgFile != null) {
            System.out.println(imgFile);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            // 保存图片字符流到数据库
            baseimg = "data:" +imgFile.getContentType() + ";base64," + base64Encoder.encode(imgFile.getBytes());
        }
        blog.setFirstPicture(baseimg);
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null) {
            redirectAttributes.addFlashAttribute("message", "操作失败！");
        } else {
            redirectAttributes.addFlashAttribute("message", "操作成功！");
        }
        return REDIRECT_LIST;
    }

    // blogs-input返回按钮
    @GetMapping("/goback")
    public String goBack() {
        return REDIRECT_LIST;
    }

    // 删除博客
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "删除成功！");
        return REDIRECT_LIST;
    }
}
