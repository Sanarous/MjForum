package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

@Data
public class UserIndexCommentsVO {

    //评论ID
    private Integer id;

    //头像地址
    private String avatar;

    //用户名
    private String username;

    //评论时间
    private String time;

    //评论内容
    private String comment;

    //评论的文章名
    private String title;

    //评论的问题ID
    private Integer questionId;

    //评论文章的内容
    private String contents;

    //评论文章的浏览量
    private Integer viewCount;

    //评论文章的评论量
    private Integer commentCount;

    //评论文章的点赞量
    private Integer likeCount;
}
