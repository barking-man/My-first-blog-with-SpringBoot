package com.mark.pojo;/*
 * @project: myblog
 * @name: Tag
 * @author: Mark
 * @Date: 2021/4/10
 * @Time: 0:18
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
@Table(name = "t_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "分类名称不能为空！")
    private String name;

    @ManyToMany(mappedBy = "tags") // 被维护端
    private List<Blog> blogs = new ArrayList<>();
}
