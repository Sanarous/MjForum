package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.pojo.CommentLike;

import java.util.List;

public interface CommentLikeService {

    /**
     * 处理评论点赞信息
     *
     * @param username        点赞用户名
     * @param commentId       评论ID
     * @param commentUsername 评论用户名
     * @param questionId      问题ID
     * @return 更新行数
     */
    ForumResult processCommentLike(String username, Integer commentId, String commentUsername, Integer questionId);

    /**
     * 查询点赞信息
     *
     * @param commentId 评论ID
     * @param likeId    用户ID
     * @return 点赞信息
     */
    CommentLike selectCommentLike(Integer commentId, Integer likeId);

    /**
     * 查询某个评论下的点赞数
     *
     * @param commentId 评论ID
     * @return 数量
     */
    Integer selectLikeCountByCommentId(Integer commentId);

    /**
     * 查询评论者的所有点赞数
     */
    List<CommentLike> selectCommentLikeByUsername(String username);
}
