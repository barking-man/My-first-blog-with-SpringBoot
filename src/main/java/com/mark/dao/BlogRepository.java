package com.mark.dao;/*
 * @project: myblog
 * @name: BlogRepository
 * @author: Mark
 * @Date: 2021/6/23
 * @Time: 20:21
 */

import com.mark.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    // 更新浏览次数
    @Transactional
    @Modifying // udpate语句需要使用@Modifying才能修改成功
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    // 在JPA中自定义sql语句时，若使用到方法，则需把方法放入function中
    // sql语句中使用了date_format方法，将其作为function的第一个参数，b.updateTime表示需要格式化的对象，%Y表示格式化后的结果（只取年份）
    // @Query中等效的SQL语句为：
    // SELECT date_format(b.updateTime, ‘%Y') AS year FROM t_blog b GROUP BY year ORDER BY year DESC
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime, '%Y') order by function('date_format',b.updateTime, '%Y') desc")
    List<String> findGroupYear();

    // 根据年份查询出博客
    // 第一个占位符?1也就是方法中传递的字符串year
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
