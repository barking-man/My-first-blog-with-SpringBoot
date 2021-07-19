package com.mark.pojo;/*
 * @project: myblog
 * @name: Comment
 * @author: Mark
 * @Date: 2021/4/10
 * @Time: 0:19
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    private boolean adminComment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @ManyToOne
    private Blog blog;
    // 本评论下方的第一层子评论
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();

    // 评论回复的上下级关系
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> comments = new ArrayList<>();
    @ManyToOne
    private Comment parentComment;
}
