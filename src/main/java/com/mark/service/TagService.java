package com.mark.service;/*
 * @project: myblog
 * @name: TagService
 * @author: Mark
 * @Date: 2021/6/24
 * @Time: 16:25
 */

import com.mark.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    void deleteTag(Long id);

    Tag updateTag(Long id, Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTag(String Ids);

    // 首页index显示tag
    List<Tag> listTagTop(Integer size);
}
