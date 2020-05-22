package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 评论点赞
 */
@Data
public class CommentLike {

    //自增主键ID
    private int id;

    //评论ID
    private Integer commentId;

    //评论者ID
    private Integer commentUid;

    //评论者用户名
//    private String commentName;

    //点赞者用户ID
    private Integer likeId;

    //点赞者用户名
//    private String likeName;

    //点赞时间
    private String time;

    //点赞状态 0-未点赞 1-已点赞
    private int status;

    //关联的问题ID
    private Integer questionId;
}
