package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.CommentLike;

import java.util.List;

public interface CommentLikeService {

    /**
     * 新增一条点赞信息
     * @param commentLike
     * @return
     */
    int insertCommentLike(CommentLike commentLike);

    /**
     * 查询点赞信息
     * @param commentId
     * @param likeName
     * @return
     */
    CommentLike selectCommentLike(Integer commentId,String likeName);

    /**
     * 更新点赞状态
     * @param id
     * @param status
     * @param time
     * @return
     */
    int updateCommentLike(int status,String time,Integer id);

    /**
     * 查询某个评论下的点赞数
     * @param commentId
     * @return
     */
    Integer selectLikeCountByCommentId(Integer commentId);

    /**
     * 查询评论者的所有点赞数
     */
    List<CommentLike> selectCommentLikeByUsername(String username);
}
