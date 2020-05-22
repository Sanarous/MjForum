package cn.bestzuo.mjforum.service;

import cn.bestzuo.mjforum.pojo.CommentReply;

import java.util.List;

public interface CommentReplyService {

    /**
     * 新增一条回复信息
     * @return
     */
    CommentReply insertCommentReply(Integer qId, String username, String content, Integer parentCommentId, String replyfor);

    /**
     * 根据评论ID查找楼中楼回复
     * @param commentId
     * @return
     */
    List<CommentReply> queryReplyByCommentId(Integer commentId);

    /**
     * 查询一个评论下所有回复数
     * @param parentCommentId
     * @return
     */
    int queryReplyNum(Integer parentCommentId);
}
