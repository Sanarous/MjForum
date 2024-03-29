package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.Comment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论Mapper
 */
@Component
public interface CommentMapper {
    /**
     * 找到某一个问题下的所有评论
     * @param questionId 问题ID
     * @return 评论信息
     */
    List<Comment> queryCommentByQuestionId(Integer questionId);

    /**
     * 插入一条评论
     * @param comment  评论信息
     * @return 更新行数
     */
    int insertCommentByQuestionId(Comment comment);

    /**
     * 根据评论者查询评论信息
     */
    List<Comment> selectCommentsByUid(Integer uid);

    /**
     * 根据评论ID查询评论信息
     * @param id  评论ID
     * @return 评论信息
     */
    Comment selectCommentByPrimaryKey(Integer id);

    /**
     * 查询同一用户在同一问题下评论的次数
     * @param uid  用户ID
     * @param questionId 问题ID
     * @return  更新行数
     */
    int selectOneUserCommentOnOneQuestionCount(Integer uid, Integer questionId);
}
