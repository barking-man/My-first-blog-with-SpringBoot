package com.mark.dao;/*
 * @project: myblog
 * @name: TagRepository
 * @author: Mark
 * @Date: 2021/6/24
 * @Time: 16:23
 */

import com.mark.pojo.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    // 根据名称查询
    Tag findByName(String name);

    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
