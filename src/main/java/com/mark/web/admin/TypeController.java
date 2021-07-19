package com.mark.web.admin;/*
 * @project: myblog
 * @name: TypeController
 * @author: Mark
 * @Date: 2021/4/14
 * @Time: 23:55
 */

import com.mark.pojo.Type;
import com.mark.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Validated
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model) {

        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    // 修改类型
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    // 删除类型
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message", "删除成功!");

        return "redirect:/admin/types";
    }

    // 新增类型
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // name名称非空验证
        if ("".equals(type.getName())) {
            bindingResult.rejectValue("name", "blankError", "type name should not be blank!");
        }

        // 验证新增的类型是否已存在
        Type t1 = typeService.getTypeByName(type.getName());
        if (t1 != null) {
            /*
            * bindingResult
            * s: 要校验的属性，要与实体类中的属性名称保持一致
            * s1: 验证的结果，可自定义
            * s2: 要返回的消息*/
            bindingResult.rejectValue("name", "nameError", "分类名称重复！");
        }
        if (bindingResult.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            redirectAttributes.addFlashAttribute("message", "新增失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/types";
    }

    // 更新类型
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult bindingResult,
                           @PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        // 验证分类名称是否为空或为null
        if ("".equals(type.getName())) {
            bindingResult.rejectValue("name", "blankError", "分类名称不能为空");
        }
        Type t1 = typeService.getTypeByName(type.getName());
        if (t1 != null) {
            bindingResult.rejectValue("name", "nameError", "分类分类名称重复");
        }
        if (bindingResult.hasErrors()) {
            return "admin/types-input";
        }

        // 更新类型
        Type t = typeService.updateType(id, type);
         if (t == null) {
            redirectAttributes.addFlashAttribute("message", "更新失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }
}
