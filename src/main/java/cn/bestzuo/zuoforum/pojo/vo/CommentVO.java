package cn.bestzuo.zuoforum.pojo.vo;

import lombok.Data;

@Data
public class CommentVO {
    //主键ID
    private Integer cId;

    //评论内容
    private String comment;

    //回复时间
    private String time;

    //回复者头像地址
    private String avatar;

    //评论者ID
    private int uid;

    //评论者用户名
    private String uname;

    //关联的问题ID
    private int questionId;

    //评论者的工作职位/学校
    private String info;

    //评论者的工作职位
    private String jobTitle;

    //评论者积分等级
    private String rate;

    //评论者积分
    private int rateScore;
}
