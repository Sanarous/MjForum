package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.CommentLike;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 点赞Mapper
 */
@Component
public interface CommentLikeMapper {

    /**
     * 新增一条点赞信息
     * @param commentLike  点赞信息实体类
     * @return 更新行数
     */
    int insertCommentLike(CommentLike commentLike);

    /**
     * 查询点赞信息
     * @param commentId 父评论ID
     * @param likeId  点赞用户ID
     * @return  点赞信息
     */
    CommentLike selectCommentLike(Integer commentId, Integer likeId);

    /**
     * 更新点赞状态
     * @param id 主键
     * @param status  点赞状态
     * @param time  点赞时间
     * @return 更新行数
     */
    int updateCommentLike(int status, String time, Integer id);

    /**
     * 查询某个评论下的点赞数
     * @param commentId 父评论ID
     * @return  数量
     */
    Integer selectLikeCountByCommentId(Integer commentId);

    /**
     * 查询评论者的所有点赞数
     */
    List<CommentLike> selectCommentLikeByUid(Integer uid);
}
