package com.mark.web.user;/*
 * @project: myblog
 * @name: BlogController
 * @author: Mark
 * @Date: 2021/4/13
 * @Time: 23:28
 */

import com.mark.pojo.Blog;
import com.mark.pojo.Tag;
import com.mark.pojo.Type;
import com.mark.pojo.User;
import com.mark.service.BlogService;
import com.mark.service.TagService;
import com.mark.service.TypeService;
import com.mark.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "userBlog";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";


    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    // 新增博客
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags" , tagService.listTag());
        return INPUT;
    }

    // 修改博客
    @GetMapping("/blogs/{id}/input")
    public String editinput(@PathVariable("id") Long id, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags" , tagService.listTag());

        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    // 新增或修改博客提交
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes, HttpSession session,
                       @RequestParam("imgFile") MultipartFile imgFile) throws IOException {
        // 存储图片字符流
        String baseimg = "";
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        // 图片上传
        if (imgFile != null) {
            BASE64Encoder base64Encoder = new BASE64Encoder();
            // 保存图片字符流到数据库
            baseimg = "data:" + imgFile.getContentType() + ";base64," + base64Encoder.encode(imgFile.getBytes());
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

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "删除成功！");
        return REDIRECT_LIST;
    }

//    @PostMapping("/fileUpload")
//    public String fileUpload(@RequestParam(value = "file") MultipartFile file, Model model, HttpServletRequest request) {
//        if (file.isEmpty()) {
//            System.out.println("文件为空！");
//        }
//        String filename = file.getOriginalFilename(); // 文件名
//        String suffixName = filename.substring(filename.lastIndexOf(".")); // 后缀名
//        String filePath = "D:\\IDEA\\workspace\\myblog\\src\\main\\resources\\static\\images"; // 保存路径
//        filename = UUID.randomUUID() + suffixName; // 新文件名
//        File dest = new File(filePath + filename);
//        if (!dest.getParentFile().exists()) {
//            dest.getParentFile().mkdirs(); // 父级目录不存在时，创建目录
//        }
//        try {
//            file.transferTo(dest);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        filename = "/imgUpload/" + filename;
//        model.addAttribute("filename", filename);
//        System.out.println("===================>");
//        System.out.println("进入BlogController");
//        return "index";
//    }
}
