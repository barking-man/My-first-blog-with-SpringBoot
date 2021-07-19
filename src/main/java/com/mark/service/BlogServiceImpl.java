package com.mark.service;/*
 * @project: myblog
 * @name: BlogServiceImpl
 * @author: Mark
 * @Date: 2021/6/23
 * @Time: 20:22
 */

import com.mark.NotFoundException;
import com.mark.dao.BlogRepository;
import com.mark.pojo.Blog;
import com.mark.pojo.Type;
import com.mark.util.MarkdownUtils;
import com.mark.util.MyBeanUtils;
import com.mark.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            /*
            * root: 将Blog对象映射成root对象，root中包含了所要处理的字段
            * criteriaQuery: 查询容器，内含所设置的查询条件
            * criteriaBuilder: 设置具体的查询条件表达式
            * */
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // predicates: 存放查询条件
                List<Predicate> predicates = new ArrayList<>();

                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    // criteriaBuilder.like(s,t) 相当于 s（参数名） like t（参数值）
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"  ));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                if (blog.getUserId() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("user").get("id"), blog.getUserId()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },  pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags"); // 将blog对象（此处即root）与tags关联起来
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long blogCounts() {
        // 返回表中blog集合的博客条数
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) { // 新增博客
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    @Override
    /*
    * blog：从前端传过来
    * */
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("不存在该篇博客！");
        }
        // P36 在将blog赋值给b时，blog是从前端接收的数据所构成的对象，里面有部分属性为空
        // 直接进行赋值的话，将使b中的置为空，因此不能将blog中为空的对象传给b
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog)); // 仅copy非空元素
        b.setUpdateTime(new Date()); // 重新设置更新时间
        return blogRepository.save(b);
    }

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();
        if (blog == null) {
            throw new NotFoundException("该博客不存在！");
        }
        // 为了不在Service中对数据库进行修改，需要重新创建一个新的blog对象
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        // 将markdown格式的内容转换为html格式
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        // 更新浏览次数
        blogRepository.updateViews(id);
        return b;
    }
}
