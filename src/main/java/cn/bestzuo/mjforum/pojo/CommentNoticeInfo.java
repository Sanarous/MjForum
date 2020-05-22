package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 评论通知实体类
 */
@Data
public class CommentNoticeInfo {
    //自增ID
    private Integer id;

    //父评论ID
    private Integer parentCommentId;

    //回复者ID
    private Integer commentId;

//    private String commentName;

    //被回复者ID
    private Integer noticeId;

//    private String noticeName;

    //回复时间
    private String time;

    //问题ID
    private Integer questionId;

    //状态 0-未阅读 1-已阅读
    private Integer status;

    //回复内容
    private String content;
}