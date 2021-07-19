package com.mark.web;/*
 * @project: myblog
 * @name: IndexController
 * @author: Mark
 * @Date: 2021/4/7
 * @Time: 0:00
 */

import com.mark.NotFoundException;
import com.mark.service.BlogService;
import com.mark.service.TagService;
import com.mark.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {


}
