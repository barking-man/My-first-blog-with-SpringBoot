package com.mark.pojo;/*
 * @project: myblog
 * @name: Type
 * @author: Mark
 * @Date: 2021/4/10
 * @Time: 0:17
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "t_type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "分类名称不能为空！")
    private String name;
    @OneToMany(mappedBy = "type") // 一种type对应多个blog，被维护
    private List<Blog> blogs = new ArrayList<>();
}
