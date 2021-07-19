package com.mark.vo;/*
 * @project: myblog
 * @name: BlogQuery
 * @author: Mark
 * @Date: 2021/6/24
 * @Time: 20:43
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* Blogs.html中进行分页查询时，将查询条件封装成一个类
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;
    private Long userId;
}
