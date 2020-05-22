package cn.bestzuo.mjforum.pojo;

import lombok.Data;

/**
 * 问题
 */
@Data
public class Question {

    //问题ID
    private Integer id;

    //问题标题
    private String title;

    //问题发布时间
    private String gmtCreate;

    //问题最后回复时间
    private String gmtModified;

    //发帖用户ID
    private Integer publisherId;

    //评论数
    private Integer commentCount;

    //阅读数
    private Integer viewCount;

    //点赞数
    private Integer likeCount;

    //文章标签
    private String tag;

    //文章补充描述
    private String description;

    //问题是否被置顶
    private int isDing;

    //问题是否被加精
    private int isJing;

    //帖子状态 0-正常 1-删除
    private int status;
}