package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

/**
 * 消息通知前端显示类
 */
@Data
public class CommentNoticeVO{
    //回复的主键自增ID
    private Integer id;

    //父评论ID
    private Integer parentCommentId;

    //回复者的头像地址
    private String commentAvatar;

    //回复者的用户名
    private String username;

    //在问题ID为qId下回复的
    private Integer questionId;

    //在标题为title问题下回复的
    private String title;

    //评论的父级ID，也即Comment的ID

    //回复时间
    private String time;

    //回复内容
    private String content;

    //回复后的阅读状态
    private int status;
}
