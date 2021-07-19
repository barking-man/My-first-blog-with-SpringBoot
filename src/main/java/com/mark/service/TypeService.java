package com.mark.service;/*
 * @project: myblog
 * @name: TypeService
 * @author: Mark
 * @Date: 2021/4/14
 * @Time: 0:18
 */

import com.mark.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    // 保存类型
    Type saveType(Type type);
    // 根据id查询类型
    Type getType(Long id);
    // 通过名称查询类型
    Type getTypeByName(String name);
    // 分页查询
    Page<Type> listType(Pageable pageable);
    // 获取所有类型
    List<Type> listType();
    // 根据size的值来确定index页面中所显示的type的数量
    List<Type> listTypeTop(Integer size);
    // 更新
    Type updateType(Long id, Type type);
    // 删除
    void deleteType(Long id);
}
