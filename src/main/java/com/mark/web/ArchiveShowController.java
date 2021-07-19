package com.mark.web;/*
 * @project: myblog
 * @name: ArchivesShowController
 * @author: Mark
 * @Date: 2021/7/8
 * @Time: 14:44
 */

import com.mark.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String showArchives(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCounts", blogService.blogCounts());
        return "archives";
    }
}
