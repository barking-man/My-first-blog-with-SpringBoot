package com.mark.web;/*
 * @project: myblog
 * @name: TypeShowController
 * @author: Mark
 * @Date: 2021/7/7
 * @Time: 22:46
 */

import com.mark.pojo.Tag;
import com.mark.service.BlogService;
import com.mark.service.TagService;
import com.mark.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 5, sort = ("updateTime"), direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable("id") Long id) {
        List<Tag> tagList = tagService.listTagTop(10000); // 选择阻构大的size，使所有的类型都能显示出来
        if (id == -1) { // id == -1 表示是从首页跳转过来的，见_fragments中的nav模块
            id = tagList.get(0).getId();
        }
        model.addAttribute("tags", tagList);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id); // 用于判断需要高亮显示的type
        return "tags";
    }
}
