package com.mark.web.admin;/*
 * @project: myblog
 * @name: TagController
 * @author: Mark
 * @Date: 2021/6/24
 * @Time: 16:57
 */

import com.mark.pojo.Tag;
import com.mark.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String showTags(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String tagsInput(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String addTags(@PathVariable("id") Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message", "删除成功！");
        return "redirect:/admin/tags";
    }

    // 新增标签
    @PostMapping("/tags")
    public String editPost(@Valid Tag tag, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if ("".equals(tag.getName())) {
            bindingResult.rejectValue("name", "blankError", "标签名不能为空！");
        }
        Tag t = tagService.getTagByName(tag.getName());
        if (t != null) {
            bindingResult.rejectValue("name", "nameError", "标签名重复！");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t1 = tagService.saveTag(tag);
        if (t1 != null) {
            redirectAttributes.addFlashAttribute("message", "新增成功！");
        } else {
            redirectAttributes.addFlashAttribute("message", "新增失败！");
        }
        return "redirect:/admin/tags";
    }

    // 更新标签
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult bindingResult,
                           @PathVariable("id") Long id,
                           RedirectAttributes ra) {
        if ("".equals(tag.getName())) {
            bindingResult.rejectValue("name", "blankError", "标签名称不能为空！");
        }
        Tag t = tagService.getTagByName(tag.getName());
        if (t != null) {
            bindingResult.rejectValue("name", "nameError", "标签名称重复！");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t1 = tagService.updateTag(id, tag);

        if (t1 != null) {
            ra.addFlashAttribute("message", "更新成功！");
        } else {
            ra.addFlashAttribute("message", "更新失败！");
        }
        return "redirect:/admin/tags";
    }
}
