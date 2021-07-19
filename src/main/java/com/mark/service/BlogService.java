package com.mark.service;/*
 * @project: myblog
 * @name: BlogService
 * @author: Mark
 * @Date: 2021/6/23
 * @Time: 20:07
 */

import com.mark.pojo.Blog;
import com.mark.vo.BlogQuery;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    // index全局搜索
    Page<Blog> listBlog(String query, Pageable pageable);

    // 从首页index跳转到tag页面时，顶端显示的标签列表查询
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    // 首页index博客推荐列表
    List<Blog> listRecommendBlogTop(Integer size);

    // 归档页博客显示
    Map<String, List<Blog>> archiveBlog();

    // 归档页显示博客条数
    Long blogCounts();

    Blog saveBlog(Blog blog);

    void deleteBlog(Long id);

    Blog updateBlog(Long id, Blog blog);

    Blog getBlog(Long id);

    // markdown转html
    Blog getAndConvert(Long id);
}
