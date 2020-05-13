package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CommentLikeMapper;
import cn.bestzuo.zuoforum.pojo.CommentLike;
import cn.bestzuo.zuoforum.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论点赞管理Service
 */
@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    /**
     * 新增一条点赞信息
     * @param commentLike
     * @return
     */
    @Override
    @Transactional
    public int insertCommentLike(CommentLike commentLike) {
        return commentLikeMapper.insertCommentLike(commentLike);
    }

    /**
     * 查询点赞信息
     * @param commentId
     * @param likeName
     * @return
     */
    @Override
    public CommentLike selectCommentLike(Integer commentId, String likeName) {
        return commentLikeMapper.selectCommentLike(commentId, likeName);
    }

    /**
     * 修改点赞状态
     * @param status
     * @param time
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int updateCommentLike(int status, String time, Integer id) {
        return commentLikeMapper.updateCommentLike(status, time, id);
    }

    @Override
    public Integer selectLikeCountByCommentId(Integer commentId) {
        return commentLikeMapper.selectLikeCountByCommentId(commentId);
    }

    @Override
    public List<CommentLike> selectCommentLikeByUsername(String username) {
        return commentLikeMapper.selectCommentLikeByUsername(username);
    }
}
