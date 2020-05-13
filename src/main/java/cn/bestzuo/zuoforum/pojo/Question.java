package cn.bestzuo.zuoforum.pojo;

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

    //发布人
    private String publisher;

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
}