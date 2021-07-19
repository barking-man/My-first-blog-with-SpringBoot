package com.mark.dao;/*
 * @project: myblog
 * @name: CommentRepository
 * @author: Mark
 * @Date: 2021/7/6
 * @Time: 14:52
 */

import com.mark.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Sort可以实现排序 ParentCommentNot表示父类为空
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
