package cn.bestzuo.mjforum.mapper;

import cn.bestzuo.mjforum.pojo.CommentReply;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 楼中楼回复Mapper
 */
@Component
public interface CommentReplyMapper {

    /**
     * 新增一条回复信息
     *
     * @param commentReply 楼中楼评论实体类
     * @return 更新行数
     */
    int insertCommentReply(CommentReply commentReply);

    /**
     * 根据评论Id查找楼中楼回复
     *
     * @param commentId 评论ID
     * @return 评论信息
     */
    List<CommentReply> queryReplyByCommentId(Integer commentId);

    /**
     * 查询一个评论下所有回复数
     *
     * @param parentCommentId 父评论ID
     * @return 数量
     */
    int queryReplyNum(Integer parentCommentId);
}
