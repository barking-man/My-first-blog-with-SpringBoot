package com.mark.dao;/*
 * @project: myblog
 * @name: TypeRepository
 * @author: Mark
 * @Date: 2021/4/14
 * @Time: 0:25
 */

import com.mark.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {

    // 根据名称查询
    Type findByName(String name);

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
