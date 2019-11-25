package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.Comment;

import java.util.List;

/**
 * 评论Mapper
 */
public interface CommentMapper {
    /**
     * 找到某一个问题下的所有评论
     * @param questionId 问题ID
     * @return
     */
    List<Comment> queryCommentByQuestionId(Integer questionId);

    /**
     * 插入一条评论
     * @param comment
     * @return
     */
    int insertCommentByQuestionId(Comment comment);

    /**
     * 根据评论者查询评论信息
     */
    List<Comment> selectCommentsByUname(String uname);

    /**
     * 根据评论ID查询评论信息
     * @param id
     * @return
     */
    Comment selectCommentByPrimaryKey(Integer id);
}
