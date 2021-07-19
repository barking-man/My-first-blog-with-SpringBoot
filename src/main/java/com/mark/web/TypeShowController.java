package com.mark.web;/*
 * @project: myblog
 * @name: TypeShowController
 * @author: Mark
 * @Date: 2021/7/7
 * @Time: 22:46
 */

import com.mark.pojo.Type;
import com.mark.service.BlogService;
import com.mark.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 5, sort = ("updateTime"), direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @PathVariable("id") Long id) {
        List<Type> typeList = typeService.listTypeTop(10000); // 选择阻构大的size，使所有的类型都能显示出来
        if (id == -1) { // id == -1 表示是从首页跳转过来的，见_fragments中的nav模块
            id = typeList.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery(); // 用来查询出高亮类型所对应的博客
        blogQuery.setTypeId(id);
        model.addAttribute("types", typeList);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id); // 用于判断需要高亮显示的type
        return "types";
    }
}
