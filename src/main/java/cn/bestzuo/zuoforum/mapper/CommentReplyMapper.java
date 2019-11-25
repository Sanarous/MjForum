package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.CommentReply;

import java.util.List;

public interface CommentReplyMapper {

    /**
     * 新增一条回复信息
     * @param commentReply
     * @return
     */
    int insertCommentReply(CommentReply commentReply);

    /**
     * 根据评论Id查找楼中楼回复
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
