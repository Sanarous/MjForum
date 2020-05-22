package cn.bestzuo.mjforum.service.impl;

import cn.bestzuo.mjforum.common.ForumResult;
import cn.bestzuo.mjforum.mapper.CommentLikeMapper;
import cn.bestzuo.mjforum.mapper.CommentMapper;
import cn.bestzuo.mjforum.mapper.QuestionMapper;
import cn.bestzuo.mjforum.mapper.UserInfoMapper;
import cn.bestzuo.mjforum.pojo.Comment;
import cn.bestzuo.mjforum.pojo.CommentLike;
import cn.bestzuo.mjforum.pojo.UserInfo;
import cn.bestzuo.mjforum.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 评论点赞管理Service
 */
@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeMapper commentLikeMapper;

    private final UserInfoMapper userInfoMapper;

    private final CommentMapper commentMapper;

    @Autowired
    public CommentLikeServiceImpl(CommentLikeMapper commentLikeMapper, UserInfoMapper userInfoMapper, QuestionMapper questionMapper, CommentMapper commentMapper) {
        this.commentLikeMapper = commentLikeMapper;
        this.userInfoMapper = userInfoMapper;
        this.commentMapper = commentMapper;
    }

    /**
     * 处理点赞信息，主要是 [点赞/取消] 操作
     *
     * @param username        点赞用户名
     * @param commentId       评论ID
     * @param commentUsername 评论用户名
     * @param questionId      问题ID
     * @return 包装结果
     */
    @Override
    public ForumResult processCommentLike(String username, Integer commentId, String commentUsername, Integer questionId) {
        //点赞者
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);

        //被点赞者
        UserInfo userInfo1 = userInfoMapper.selectUserInfoByName(commentUsername);

        if (userInfo == null || userInfo1 == null) {
            return new ForumResult(400, "用户不存在", null);
        }

        Comment comment = commentMapper.selectCommentByPrimaryKey(commentId);
        if (comment == null) {
            return new ForumResult(400, "评论信息不存在", null);
        }

        //先查询点赞信息
        CommentLike commentLike = commentLikeMapper.selectCommentLike(commentId, userInfo.getUId());
        if (commentLike == null) {
            //第一次点赞
            CommentLike like = new CommentLike();
            like.setCommentId(commentId);
            like.setCommentUid(userInfo1.getUId());
            like.setLikeId(userInfo.getUId());
            like.setStatus(1);
            like.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            like.setQuestionId(questionId);

            commentLikeMapper.insertCommentLike(like);
            return new ForumResult(200, "点赞成功", 1);
        } else {
            //查询信息
            if (commentLike.getStatus() == 0) {
                //点赞
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                int i = commentLikeMapper.updateCommentLike(1, time, commentLike.getId());
                return i > 0 ? new ForumResult(200, "更新成功", 1) : new ForumResult(500, "更新失败", null);
            } else {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                int i = commentLikeMapper.updateCommentLike(0, time, commentLike.getId());
                return i > 0 ? new ForumResult(200, "更新成功", 0) : new ForumResult(500, "更新失败", null);
            }
        }
    }

    /**
     * 查询点赞信息
     *
     * @param commentId 评论ID
     * @param likeId    点赞用户名
     * @return 评论点赞信息实体类
     */
    @Override
    public CommentLike selectCommentLike(Integer commentId, Integer likeId) {
        return commentLikeMapper.selectCommentLike(commentId, likeId);
    }

    @Override
    public Integer selectLikeCountByCommentId(Integer commentId) {
        return commentLikeMapper.selectLikeCountByCommentId(commentId);
    }

    @Override
    public List<CommentLike> selectCommentLikeByUsername(String username) {
        UserInfo userInfo = userInfoMapper.selectUserInfoByName(username);
        return commentLikeMapper.selectCommentLikeByUid(userInfo.getUId());
    }
}
