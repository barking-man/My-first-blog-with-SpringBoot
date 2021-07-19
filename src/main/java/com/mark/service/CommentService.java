package com.mark.service;/*
 * @project: myblog
 * @name: CommentsService
 * @author: Mark
 * @Date: 2021/7/6
 * @Time: 14:46
 */

import com.mark.pojo.Comment;

import java.util.List;

public interface CommentService {

    // 获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    // 保存comment
    Comment saveComment(Comment comment);


}
