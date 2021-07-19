package com.mark.web;/*
 * @project: myblog
 * @name: AboutController
 * @author: Mark
 * @Date: 2021/7/8
 * @Time: 15:35
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
