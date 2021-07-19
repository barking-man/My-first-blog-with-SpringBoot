package com.mark.web;/*
 * @project: myblog
 * @name: TestController
 * @author: Mark
 * @Date: 2021/7/16
 * @Time: 1:18
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "testUpload";
    }
}
