package cn.bestzuo.mjforum.pojo.vo;

import lombok.Data;

@Data
public class CommentLikeVO {

    //自增主键ID
    private Integer id;

    //点赞者ID
    private Integer uid;

    //点赞者用户名
    private String username;

    //点赞者头像
    private String avatar;

    //评论内容
    private String content;

    //关联的问题ID
    private Integer questionId;

    //关联的问题标题
    private String title;

    //点赞时间
    private String time;
}
