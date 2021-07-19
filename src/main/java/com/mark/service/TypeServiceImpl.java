package com.mark.service;/*
 * @project: myblog
 * @name: TypeServiceImpl
 * @author: Mark
 * @Date: 2021/4/14
 * @Time: 0:23
 */

import com.mark.NotFoundException;
import com.mark.dao.TypeRepository;
import com.mark.pojo.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        // 字段blogs.size是指排序的依据，在Type类中是存在blogs这样一个集合，因此可以根据集合的长度，即blogs.size排序
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable =PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        /*
        * BeanUtils.copyProperties(source,target)
        * 将source中的属性值，赋给target中的属性值，避免了对象中的属性使用set、get方法挨个赋值的问题
        * source: 源对象
        * target: 目的对象
        * */
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
