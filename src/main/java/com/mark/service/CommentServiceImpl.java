package com.mark.service;/*
 * @project: myblog
 * @name: CommentServiceImpl
 * @author: Mark
 * @Date: 2021/7/6
 * @Time: 14:50
 */

import com.mark.dao.CommentRepository;
import com.mark.pojo.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        // 判断是否有父级评论
        Long parentCommentId = comment.getParentComment().getId(); // parentCommentId默认为-1，因此不用担心空指针异常
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    // 循环每个顶级的评论节点
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1) {
                // 循环迭代找出子代
                recursively(reply1);
            }
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys); // 将comment下的所有回复（包括子评论的回复）全都平级摆放到集合中
            // 清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合，全局变量
    private List<Comment> tempReplys = new ArrayList<>();

    // 循环迭代，找出子评论
    private void recursively(Comment comment) {
        tempReplys.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
